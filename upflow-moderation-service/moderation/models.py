from datetime import datetime
from typing import Optional
from pydantic import BaseModel, Field


class FeedbackSubmittedEvent(BaseModel):
    feedback_id: str
    content: str
    tenant_id: str
    department_id: Optional[str] = None
    submitted_at: datetime
    metadata: dict = Field(default_factory=dict)

    @classmethod
    def from_avro(cls, data: dict) -> "FeedbackSubmittedEvent":
        if not isinstance(data["submitted_at"], datetime):
            data["submitted_at"] = datetime.fromtimestamp(data["submitted_at"] / 1000)
        return cls(**data)


class FeedbackModeratedEvent(BaseModel):
    feedback_id: str
    original_content: str
    moderated_content: str
    embedding: list[float]
    pii_detected: bool
    names_detected: list[str]
    flagged: bool
    flag_reason: Optional[str] = None
    severity: str = "low"
    tenant_id: str
    department_id: Optional[str] = None
    moderated_at: datetime

    def to_avro_dict(self) -> dict:
        data = self.model_dump()
        data["moderated_at"] = int(data["moderated_at"].timestamp() * 1000)
        data["severity"] = data["severity"].upper()
        return data


class FeedbackFlaggedEvent(BaseModel):
    feedback_id: str
    reason: str
    severity: str
    tenant_id: str
    flagged_at: datetime

    def to_avro_dict(self) -> dict:
        data = self.model_dump()
        data["flagged_at"] = int(data["flagged_at"].timestamp() * 1000)
        data["severity"] = data["severity"].upper()
        return data
