import pytest
from unittest.mock import MagicMock
from moderation.services.pii import PresidioPIIDetector
from moderation.services.llm import LLMService, ModerationDecision
from moderation.services.name_filter import SpacyNameFilter
from moderation.services.moderation import ModerationService


@pytest.fixture
def pii_detector():
    return PresidioPIIDetector(language="pt")


@pytest.fixture
def name_filter():
    return SpacyNameFilter(model_name="pt_core_news_lg")


@pytest.fixture
def llm_service():
    mock = MagicMock(spec=LLMService)
    mock.moderate_content.return_value = ModerationDecision(
        should_flag=False,
        reason=None,
        severity="low"
    )
    mock.generate_embedding.return_value = [0.1] * 768
    return mock


@pytest.fixture
def moderation_service(pii_detector, name_filter, llm_service):
    return ModerationService(pii_detector, name_filter, llm_service)


def test_detect_pii(pii_detector):
    text = "Meu email é joao@example.com e meu CPF é 123.456.789-00"
    pii = pii_detector.detect(text)
    assert len(pii) > 0


def test_anonymize_pii(pii_detector):
    text = "Meu email é joao@example.com"
    anonymized = pii_detector.anonymize(text)
    assert "joao@example.com" not in anonymized
    assert "[REDACTED]" in anonymized


def test_moderate_content(moderation_service):
    text = "Meu email é joao@example.com e este feedback é importante"
    result = moderation_service.moderate(text)

    assert result.moderated_content
    assert isinstance(result.pii_detected, bool)
    assert isinstance(result.names_detected, list)
    assert isinstance(result.flagged, bool)
    assert isinstance(result.severity, str)


def test_moderated_event_to_avro():
    from datetime import datetime, timezone
    from moderation.models import FeedbackModeratedEvent

    event = FeedbackModeratedEvent(
        feedback_id="123",
        original_content="test",
        moderated_content="test",
        embedding=[0.1, 0.2],
        pii_detected=False,
        names_detected=[],
        flagged=False,
        severity="low",
        tenant_id="tenant-1",
        moderated_at=datetime.now(timezone.utc)
    )

    avro_dict = event.to_avro_dict()

    assert avro_dict["severity"] == "LOW"
    assert isinstance(avro_dict["moderated_at"], int)


def test_flagged_event_to_avro():
    from datetime import datetime, timezone
    from moderation.models import FeedbackFlaggedEvent

    event = FeedbackFlaggedEvent(
        feedback_id="123",
        reason="test",
        severity="high",
        tenant_id="tenant-1",
        flagged_at=datetime.now(timezone.utc)
    )

    avro_dict = event.to_avro_dict()

    assert avro_dict["severity"] == "HIGH"
    assert isinstance(avro_dict["flagged_at"], int)
