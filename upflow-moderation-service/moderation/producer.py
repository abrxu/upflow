import logging
from confluent_kafka import SerializingProducer
from confluent_kafka.serialization import StringSerializer
from confluent_kafka.schema_registry.avro import AvroSerializer

from moderation.config import settings
from moderation.models import FeedbackModeratedEvent, FeedbackFlaggedEvent

logger = logging.getLogger(__name__)


class KafkaProducer:
    def __init__(
        self,
        moderated_serializer: AvroSerializer,
        flagged_serializer: AvroSerializer
    ):
        base_config = {
            'bootstrap.servers': settings.kafka_bootstrap_servers,
            'compression.type': 'snappy',
            'linger.ms': 10,
            'key.serializer': StringSerializer('utf_8'),
        }

        self.moderated_producer = SerializingProducer({
            **base_config,
            'value.serializer': moderated_serializer,
        })

        self.flagged_producer = SerializingProducer({
            **base_config,
            'value.serializer': flagged_serializer,
        })

        self._pending_messages = 0

    def _delivery_report(self, err, msg):
        self._pending_messages -= 1

        if err:
            logger.error(f"Message delivery failed to {msg.topic()}: {err}")
        else:
            logger.debug(f"Message delivered to {msg.topic()} [{msg.partition()}] @ offset {msg.offset()}")

    def send_moderated(self, key: str, event: FeedbackModeratedEvent):
        try:
            self._pending_messages += 1
            self.moderated_producer.produce(
                settings.kafka_output_topic,
                key=key,
                value=event.to_avro_dict(),
                on_delivery=self._delivery_report
            )
            self.moderated_producer.poll(0)

        except BufferError:
            logger.warning("Producer queue full, flushing...")
            self.moderated_producer.flush()
            self.send_moderated(key, event)

        except Exception as e:
            logger.error(f"Failed to send moderated event: {e}")
            raise

    def send_flagged(self, key: str, event: FeedbackFlaggedEvent):
        try:
            self._pending_messages += 1
            self.flagged_producer.produce(
                settings.kafka_flagged_topic,
                key=key,
                value=event.to_avro_dict(),
                on_delivery=self._delivery_report
            )
            self.flagged_producer.poll(0)

        except BufferError:
            logger.warning("Producer queue full, flushing...")
            self.flagged_producer.flush()
            self.send_flagged(key, event)

        except Exception as e:
            logger.error(f"Failed to send flagged event: {e}")
            raise

    def flush(self, timeout: float = 10.0):
        pending = self._pending_messages
        if pending > 0:
            logger.info(f"Flushing {pending} pending messages...")
            self.moderated_producer.flush(timeout=timeout)
            self.flagged_producer.flush(timeout=timeout)

    def close(self):
        self.flush()
        logger.info("Producer closed")

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.close()
