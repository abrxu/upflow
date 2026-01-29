import logging
import json
from typing import Optional
from dataclasses import dataclass

from openai import OpenAI

logger = logging.getLogger(__name__)


@dataclass
class ModerationDecision:
    should_flag: bool
    reason: Optional[str]
    severity: str


@dataclass
class SanitizationResult:
    sanitized_content: str
    removed_names: list[str]
    removed_profanity: bool


SANITIZATION_PROMPT = """You are a content sanitization system for anonymous employee feedback.

Your task is to clean the feedback by:
1. Removing ALL personal names (replace with [PESSOA])
2. Removing profanity and offensive words (replace with [REMOVIDO])
3. Keep the core message and sentiment intact
4. Preserve the feedback's meaning while making it anonymous and professional

Original feedback:
"{text}"

Respond in JSON format:
{{
    "sanitized_content": "The cleaned version of the feedback",
    "removed_names": ["list", "of", "names", "found"],
    "removed_profanity": true/false
}}"""


MODERATION_PROMPT = """Analyze the following employee feedback and determine if it should be flagged for review.

Flag the content if it contains:
- Severe threats or violence
- Discriminatory language (racism, sexism, etc.)
- Spam or completely irrelevant content
- Content that could identify specific individuals despite sanitization

Note: Minor profanity that was already sanitized should NOT cause flagging.

Employee feedback (already sanitized):
"{text}"

Respond in JSON format:
{{
    "should_flag": true/false,
    "reason": "Brief explanation if flagged, null otherwise",
    "severity": "low/medium/high/critical"
}}"""


class LLMService:
    def __init__(self, base_url: str, api_key: str):
        self.client = OpenAI(base_url=base_url, api_key=api_key)

    def sanitize_content(self, text: str) -> SanitizationResult:
        try:
            response = self.client.chat.completions.create(
                model="default",
                messages=[
                    {
                        "role": "system",
                        "content": "You are a content sanitization system. Remove names and profanity while preserving the feedback's meaning."
                    },
                    {
                        "role": "user",
                        "content": SANITIZATION_PROMPT.format(text=text)
                    }
                ],
                response_format={"type": "json_object"},
                temperature=0.1,
            )

            result = json.loads(response.choices[0].message.content)
            logger.info(f"LLM sanitization: removed {len(result.get('removed_names', []))} names, profanity={result.get('removed_profanity', False)}")

            return SanitizationResult(
                sanitized_content=result.get("sanitized_content", text),
                removed_names=result.get("removed_names", []),
                removed_profanity=result.get("removed_profanity", False)
            )
        except Exception as e:
            logger.error(f"Error in LLM sanitization: {e}")
            return SanitizationResult(sanitized_content=text, removed_names=[], removed_profanity=False)

    def moderate_content(self, text: str) -> ModerationDecision:
            try:
                response = self.client.chat.completions.create(
                    model="default",
                    messages=[
                        {
                            "role": "system",
                            "content": "You are a content moderation system for employee feedback. Be strict but fair."
                        },
                        {
                            "role": "user",
                            "content": MODERATION_PROMPT.format(text=text)
                        }
                    ],
                    response_format={"type": "json_object"},
                    temperature=0.3,
                )

                result = json.loads(response.choices[0].message.content)

                raw_severity = result.get("severity") or "low"
                severity = str(raw_severity).upper()

                if severity not in ["LOW", "MEDIUM", "HIGH", "CRITICAL"]:
                    severity = "LOW"

                return ModerationDecision(
                    should_flag=result.get("should_flag", False),
                    reason=result.get("reason"),
                    severity=severity
                )
            except Exception as e:
                logger.error(f"Error in LLM moderation: {e}")
                return ModerationDecision(should_flag=False, reason=None, severity="LOW")

    def generate_embedding(self, text: str) -> Optional[list[float]]:
        try:
            response = self.client.embeddings.create(
                model="embedding",
                input=text
            )
            return response.data[0].embedding
        except Exception as e:
            logger.error(f"Error generating embedding: {e}")
            return None
