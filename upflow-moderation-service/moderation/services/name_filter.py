import re
from typing import Protocol, Optional
import spacy
from spacy.language import Language


class NameFilter(Protocol):
    def extract_names(self, text: str) -> list[str]:
        ...

    def filter_names(self, text: str) -> str:
        ...


class SpacyNameFilter:
    def __init__(self, model_name: str = "pt_core_news_lg"):
        self.nlp: Optional[Language] = None
        try:
            self.nlp = spacy.load(model_name)
        except OSError:
            pass

    def extract_names(self, text: str) -> list[str]:
        if not self.nlp:
            return []

        doc = self.nlp(text)
        names = [ent.text for ent in doc.ents if ent.label_ == "PER"]
        return list(set(names))

    def filter_names(self, text: str) -> str:
        if not self.nlp:
            return text

        names = self.extract_names(text)
        filtered_text = text
        for name in names:
            filtered_text = re.sub(
                rf"\b{re.escape(name)}\b",
                "[NAME]",
                filtered_text,
                flags=re.IGNORECASE
            )
        return filtered_text
