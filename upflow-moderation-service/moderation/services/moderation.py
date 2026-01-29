import logging
from dataclasses import dataclass
from typing import Optional

from moderation.services.pii import PIIDetector
from moderation.services.name_filter import NameFilter
from moderation.services.llm import LLMService

logger = logging.getLogger(__name__)


@dataclass
class ModerationResult:
    moderated_content: str
    embedding: list[float]
    pii_detected: bool
    names_detected: list[str]
    flagged: bool
    flag_reason: Optional[str]
    severity: str


class ModerationService:
    def __init__(
        self,
        pii_detector: PIIDetector,
        name_filter: NameFilter,
        llm_service: LLMService,
    ):
        self.pii_detector = pii_detector
        self.name_filter = name_filter
        self.llm_service = llm_service

    def moderate(self, content: str) -> ModerationResult:
        logger.info(f"Starting moderation pipeline for content: {content[:50]}...")

        pii_detections = self.pii_detector.detect(content)
        moderated_content = self.pii_detector.anonymize(content)
        logger.info(f"PII pass: {len(pii_detections)} detections")

        spacy_names = self.name_filter.extract_names(content)
        moderated_content = self.name_filter.filter_names(moderated_content)
        logger.info(f"spaCy pass: {len(spacy_names)} names found")

        sanitization = self.llm_service.sanitize_content(moderated_content)
        moderated_content = sanitization.sanitized_content
        llm_names = sanitization.removed_names
        logger.info(f"LLM sanitization pass: {len(llm_names)} names, profanity={sanitization.removed_profanity}")

        all_names = list(set(spacy_names + llm_names))
        has_pii = len(pii_detections) > 0

        llm_decision = self.llm_service.moderate_content(moderated_content)
        logger.info(f"LLM moderation: flagged={llm_decision.should_flag}, severity={llm_decision.severity}")

        embedding = self.llm_service.generate_embedding(moderated_content) or []
        logger.info(f"Embedding generated: {len(embedding)} dimensions")

        logger.info(f"Final moderated content: {moderated_content[:100]}...")

        return ModerationResult(
            moderated_content=moderated_content,
            embedding=embedding,
            pii_detected=has_pii,
            names_detected=all_names,
            flagged=llm_decision.should_flag,
            flag_reason=llm_decision.reason,
            severity=llm_decision.severity,
        )
