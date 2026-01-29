# UpFlow Moderation Service

Async worker that consumes feedback events, performs AI-powered content moderation, and publishes moderated events.

## Features

- **PII Detection & Anonymization**: Uses Presidio to detect and redact sensitive information
- **Name Filtering**: Extracts and filters person names using spaCy NLP
- **LLM-Powered Moderation**: Context-aware content flagging with severity classification
- **LiteLLM Proxy**: Unified LLM access with automatic fallback (Gemini → OpenAI)
- **Embedding Generation**: Creates vector embeddings for semantic search
- **Event-Driven**: Kafka consumer/producer pattern with configurable topics

## Setup

```bash
# Install dependencies
uv sync

# Download spaCy Portuguese model
uv run python -m spacy download pt_core_news_lg

# Copy and configure environment
cp .env.example .env
# Edit .env with your configuration

# Run the service
uv run python -m moderation
```

## Configuration

Environment variables (see `.env.example`):

**Kafka:**
- `KAFKA_BOOTSTRAP_SERVERS`: Kafka broker address (default: localhost:9092)
- `KAFKA_INPUT_TOPIC`: Topic to consume from (default: feedback.submitted)
- `KAFKA_OUTPUT_TOPIC`: Topic to produce moderated events (default: feedback.moderated)
- `KAFKA_FLAGGED_TOPIC`: Topic for flagged content (default: feedback.flagged)

**LLM (via LiteLLM Proxy):**
- `LLM_BASE_URL`: LiteLLM proxy URL (default: http://localhost:4000)
- `LLM_API_KEY`: LiteLLM master key (default: sk-upflow-dev)

**Other:**
- `LOG_LEVEL`: Logging level (default: INFO)

## Testing

```bash
uv run pytest
```

## Architecture

```
moderation/
├── __init__.py
├── __main__.py          # Entry point (python -m moderation)
├── config.py            # Settings with pydantic
├── models.py            # Event models (Pydantic)
├── consumer.py          # Kafka consumer
├── producer.py          # Kafka producer
├── dependencies.py      # Dependency injection container
└── services/
    ├── pii.py              # PII detection/anonymization
    ├── name_filter.py      # Name extraction/filtering
    ├── llm.py              # LLM service (OpenAI SDK → LiteLLM)
    └── moderation.py       # Orchestrates all services
```

**Moderation Flow**:
1. Consumer receives feedback event
2. PII Detection → Anonymization
3. Name Extraction → Filtering
4. LLM Moderation via LiteLLM Proxy
5. Generate embeddings via LiteLLM Proxy
6. Producer publishes to topics

**LiteLLM Proxy (centralized)**:
```
┌─────────────────────┐
│  Moderation Service │
└──────────┬──────────┘
           │ OpenAI SDK
           ▼
┌─────────────────────┐
│    LiteLLM Proxy    │
│  - Fallback chain   │
│  - Rate limiting    │
│  - Logging          │
└──────────┬──────────┘
     ┌─────┴─────┐
     ▼           ▼
  Gemini      OpenAI
```
