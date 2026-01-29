import logging
from typing import Protocol
from presidio_analyzer import AnalyzerEngine
from presidio_anonymizer import AnonymizerEngine
from presidio_anonymizer.entities import OperatorConfig

logger = logging.getLogger(__name__)


class PIIDetection:
    entity_type: str
    start: int
    end: int
    score: float


class PIIDetector(Protocol):
    def detect(self, text: str) -> list[PIIDetection]:
        ...

    def anonymize(self, text: str) -> str:
        ...


class PresidioPIIDetector:
    def __init__(self, language: str = "en"):
        self.language = language
        self.analyzer = AnalyzerEngine()
        self.anonymizer = AnonymizerEngine()
        self.entities = ["PERSON", "EMAIL_ADDRESS", "PHONE_NUMBER", "CREDIT_CARD"]

    def detect(self, text: str) -> list[dict]:
        try:
            results = self.analyzer.analyze(
                text=text,
                language=self.language,
                entities=self.entities
            )
            return [
                {
                    "type": r.entity_type,
                    "start": r.start,
                    "end": r.end,
                    "score": r.score
                }
                for r in results
            ]
        except ValueError as e:
            logger.warning(f"PII detection skipped: {e}")
            return []

    def anonymize(self, text: str) -> str:
        try:
            results = self.analyzer.analyze(text=text, language=self.language)
            if not results:
                return text
            anonymized = self.anonymizer.anonymize(
                text=text,
                analyzer_results=results,
                operators={"DEFAULT": OperatorConfig("replace", {"new_value": "[REDACTED]"})}
            )
            return anonymized.text
        except ValueError as e:
            logger.warning(f"PII anonymization skipped: {e}")
            return text
