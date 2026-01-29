from pathlib import Path
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroSerializer, AvroDeserializer

from moderation.config import settings


def _find_schemas_dir() -> Path:
    candidates = [
        Path("/app/schemas/avro"),
        Path(__file__).parent.parent.parent.parent / "schemas" / "avro",
        Path(__file__).parent.parent / "schemas" / "avro",
    ]
    for path in candidates:
        if path.exists():
            return path
    raise FileNotFoundError(f"Schemas directory not found. Tried: {candidates}")


SCHEMAS_DIR = _find_schemas_dir()


def load_schema(filename: str) -> str:
    return (SCHEMAS_DIR / filename).read_text()


def get_schema_registry_client() -> SchemaRegistryClient:
    return SchemaRegistryClient({"url": settings.kafka_schema_registry_url})


def get_submitted_deserializer(sr_client: SchemaRegistryClient) -> AvroDeserializer:
    schema_str = load_schema("feedback-submitted.avsc")
    return AvroDeserializer(sr_client, schema_str)


def get_moderated_serializer(sr_client: SchemaRegistryClient) -> AvroSerializer:
    schema_str = load_schema("feedback-moderated.avsc")
    return AvroSerializer(sr_client, schema_str)


def get_flagged_serializer(sr_client: SchemaRegistryClient) -> AvroSerializer:
    schema_str = load_schema("feedback-flagged.avsc")
    return AvroSerializer(sr_client, schema_str)
