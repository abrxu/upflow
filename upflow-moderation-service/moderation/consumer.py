import logging
import signal
import sys
from datetime import datetime, timezone
from confluent_kafka import DeserializingConsumer, KafkaError, KafkaException
from confluent_kafka.serialization import StringDeserializer
from confluent_kafka.schema_registry.avro import AvroDeserializer

from moderation.config import settings
from moderation.models import FeedbackSubmittedEvent, FeedbackModeratedEvent, FeedbackFlaggedEvent
from moderation.services.moderation import ModerationService
from moderation.producer import KafkaProducer

logger = logging.getLogger(__name__)


class FeedbackConsumer:
    def __init__(
        self,
        moderation_service: ModerationService,
        producer: KafkaProducer,
        submitted_deserializer: AvroDeserializer
    ):
        self.consumer = DeserializingConsumer({
            'bootstrap.servers': settings.kafka_bootstrap_servers,
            'group.id': settings.kafka_group_id,
            'auto.offset.reset': 'earliest',
            'enable.auto.commit': True,
            'session.timeout.ms': 30000,
            'heartbeat.interval.ms': 10000,
            'key.deserializer': StringDeserializer('utf_8'),
            'value.deserializer': submitted_deserializer,
        })

        self.producer = producer
        self.moderation_service = moderation_service
        self.consumer.subscribe([settings.kafka_input_topic])
        self.running = True

        signal.signal(signal.SIGINT, self._shutdown_handler)
        signal.signal(signal.SIGTERM, self._shutdown_handler)

    def _shutdown_handler(self, signum, frame):
        logger.info(f"Received signal {signum}, shutting down gracefully...")
        self.running = False

    def process_message(self, message_value: dict):
        try:
            event = FeedbackSubmittedEvent.from_avro(message_value)
            logger.info(f"Processing feedback {event.feedback_id} from tenant {event.tenant_id}")

            moderation_result = self.moderation_service.moderate(event.content)

            now = datetime.now(timezone.utc)

            moderated_event = FeedbackModeratedEvent(
                feedback_id=event.feedback_id,
                original_content=event.content,
                moderated_content=moderation_result.moderated_content,
                embedding=moderation_result.embedding,
                pii_detected=moderation_result.pii_detected,
                names_detected=moderation_result.names_detected,
                flagged=moderation_result.flagged,
                flag_reason=moderation_result.flag_reason,
                severity=moderation_result.severity,
                tenant_id=event.tenant_id,
                department_id=event.department_id,
                moderated_at=now
            )

            self.producer.send_moderated(event.feedback_id, moderated_event)

            if moderation_result.flagged:
                flagged_event = FeedbackFlaggedEvent(
                    feedback_id=event.feedback_id,
                    reason=moderation_result.flag_reason or "Unknown",
                    severity=moderation_result.severity,
                    tenant_id=event.tenant_id,
                    flagged_at=now
                )

                self.producer.send_flagged(event.feedback_id, flagged_event)

                logger.warning(
                    f"Flagged feedback {event.feedback_id}: "
                    f"{moderation_result.flag_reason} (severity: {moderation_result.severity})"
                )

            logger.info(f"Successfully processed feedback {event.feedback_id}")

        except Exception as e:
            logger.error(f"Error processing message: {e}", exc_info=True)
            raise

    def start(self):
        logger.info(f"Starting consumer for topic: {settings.kafka_input_topic}")
        logger.info(f"Consumer group: {settings.kafka_group_id}")

        try:
            while self.running:
                msg = self.consumer.poll(timeout=1.0)

                if msg is None:
                    continue

                if msg.error():
                    if msg.error().code() == KafkaError._PARTITION_EOF:
                        logger.debug(f"Reached end of partition {msg.partition()}")
                        continue
                    else:
                        logger.error(f"Consumer error: {msg.error()}")
                        raise KafkaException(msg.error())

                try:
                    self.process_message(msg.value())

                except Exception as e:
                    logger.error(f"Error processing message: {e}", exc_info=True)

        except KeyboardInterrupt:
            logger.info("Interrupted by user")
        except Exception as e:
            logger.error(f"Fatal error in consumer loop: {e}", exc_info=True)
            sys.exit(1)
        finally:
            logger.info("Closing consumer...")
            self.consumer.close()
            self.producer.close()
            logger.info("Consumer closed gracefully")
