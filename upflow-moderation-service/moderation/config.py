from typing import Literal
from pydantic import Field
from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
        case_sensitive=False
    )

    kafka_bootstrap_servers: str = Field(default="localhost:9092")
    kafka_schema_registry_url: str = Field(default="http://localhost:8081")
    kafka_group_id: str = Field(default="moderation-service")
    kafka_input_topic: str = Field(default="feedback.submitted")
    kafka_output_topic: str = Field(default="feedback.moderated")
    kafka_flagged_topic: str = Field(default="feedback.flagged")

    llm_base_url: str = Field(default="http://localhost:4000")
    llm_api_key: str = Field(default="sk-upflow-dev")

    log_level: Literal["DEBUG", "INFO", "WARNING", "ERROR"] = Field(default="INFO")


settings = Settings()
