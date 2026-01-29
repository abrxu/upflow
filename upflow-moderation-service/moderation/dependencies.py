import logging
from typing import Optional
from confluent_kafka.schema_registry import SchemaRegistryClient

from moderation.config import settings
from moderation.services.pii import PresidioPIIDetector
from moderation.services.name_filter import SpacyNameFilter
from moderation.services.llm import LLMService
from moderation.services.moderation import ModerationService
from moderation.avro import (
    get_schema_registry_client,
    get_submitted_deserializer,
    get_moderated_serializer,
    get_flagged_serializer,
)
from moderation.producer import KafkaProducer

logger = logging.getLogger(__name__)


_pii_detector: Optional[PresidioPIIDetector] = None
_name_filter: Optional[SpacyNameFilter] = None
_llm_service: Optional[LLMService] = None
_moderation_service: Optional[ModerationService] = None
_sr_client: Optional[SchemaRegistryClient] = None
_kafka_producer: Optional[KafkaProducer] = None


def get_pii_detector() -> PresidioPIIDetector:
    global _pii_detector
    if _pii_detector is None:
        logger.info("Initializing PII Detector...")
        _pii_detector = PresidioPIIDetector(language="pt")
    return _pii_detector


def get_name_filter() -> SpacyNameFilter:
    global _name_filter
    if _name_filter is None:
        logger.info("Initializing Name Filter (this may take a while)...")
        _name_filter = SpacyNameFilter(model_name="pt_core_news_lg")
    return _name_filter


def get_llm_service() -> LLMService:
    global _llm_service
    if _llm_service is None:
        logger.info(f"Initializing LLM Service (LiteLLM @ {settings.llm_base_url})...")
        _llm_service = LLMService(
            base_url=settings.llm_base_url,
            api_key=settings.llm_api_key
        )
    return _llm_service


def get_sr_client() -> SchemaRegistryClient:
    global _sr_client
    if _sr_client is None:
        logger.info(f"Initializing Schema Registry client ({settings.kafka_schema_registry_url})...")
        _sr_client = get_schema_registry_client()
    return _sr_client


def get_kafka_producer() -> KafkaProducer:
    global _kafka_producer
    if _kafka_producer is None:
        logger.info("Initializing Kafka Producer with Avro serializers...")
        sr_client = get_sr_client()
        _kafka_producer = KafkaProducer(
            moderated_serializer=get_moderated_serializer(sr_client),
            flagged_serializer=get_flagged_serializer(sr_client),
        )
    return _kafka_producer


def get_moderation_service() -> ModerationService:
    global _moderation_service
    if _moderation_service is None:
        logger.info("Initializing Moderation Service...")
        _moderation_service = ModerationService(
            pii_detector=get_pii_detector(),
            name_filter=get_name_filter(),
            llm_service=get_llm_service(),
        )
        logger.info("Moderation Service ready")
    return _moderation_service


def reset_dependencies():
    global _pii_detector, _name_filter, _llm_service, _moderation_service, _sr_client, _kafka_producer
    _pii_detector = None
    _name_filter = None
    _llm_service = None
    _moderation_service = None
    _sr_client = None
    _kafka_producer = None
