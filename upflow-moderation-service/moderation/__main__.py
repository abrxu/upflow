import logging
import sys
from moderation.config import settings
from moderation.consumer import FeedbackConsumer
from moderation.dependencies import (
    get_moderation_service,
    get_kafka_producer,
    get_sr_client,
)
from moderation.avro import get_submitted_deserializer

logging.basicConfig(
    level=getattr(logging, settings.log_level),
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.StreamHandler(sys.stdout)
    ]
)

logger = logging.getLogger(__name__)


def main():
    logger.info("=" * 60)
    logger.info("Starting UpFlow Moderation Service")
    logger.info("=" * 60)

    logger.info(f"Kafka Servers:    {settings.kafka_bootstrap_servers}")
    logger.info(f"Schema Registry:  {settings.kafka_schema_registry_url}")
    logger.info(f"Input Topic:      {settings.kafka_input_topic}")
    logger.info(f"Output Topic:     {settings.kafka_output_topic}")
    logger.info(f"Flagged Topic:    {settings.kafka_flagged_topic}")
    logger.info(f"LLM Proxy:        {settings.llm_base_url}")
    logger.info("=" * 60)

    try:
        moderation_service = get_moderation_service()
        producer = get_kafka_producer()
        sr_client = get_sr_client()
        submitted_deserializer = get_submitted_deserializer(sr_client)

        consumer = FeedbackConsumer(
            moderation_service=moderation_service,
            producer=producer,
            submitted_deserializer=submitted_deserializer
        )
        consumer.start()

    except Exception as e:
        logger.error(f"Fatal error: {e}", exc_info=True)
        sys.exit(1)


if __name__ == "__main__":
    main()
