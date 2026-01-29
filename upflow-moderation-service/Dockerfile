FROM python:3.12-slim AS builder

COPY --from=ghcr.io/astral-sh/uv:latest /uv /usr/local/bin/uv

WORKDIR /app

COPY upflow-moderation-service/pyproject.toml upflow-moderation-service/uv.lock ./

RUN uv sync --frozen --no-dev

RUN uv pip install pip && uv run python -m spacy download pt_core_news_lg

FROM python:3.12-slim

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    libgomp1 \
    && rm -rf /var/lib/apt/lists/*

RUN groupadd -r appgroup && useradd -r -g appgroup appuser

COPY --from=builder /app/.venv /app/.venv

COPY upflow-moderation-service/moderation moderation
COPY schemas schemas

RUN chown -R appuser:appgroup /app

USER appuser

ENV PATH="/app/.venv/bin:$PATH"
ENV PYTHONUNBUFFERED=1
ENV PYTHONDONTWRITEBYTECODE=1

HEALTHCHECK --interval=30s --timeout=10s --start-period=120s --retries=3 \
    CMD python -c "from moderation.config import settings; print('ok')" || exit 1

CMD ["python", "-m", "moderation"]
