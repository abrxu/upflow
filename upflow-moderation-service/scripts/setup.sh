#!/bin/bash

set -e

echo "Setting up Moderation Service..."

echo "Installing dependencies..."
uv sync

echo "Downloading spaCy Portuguese model..."
uv run python -m spacy download pt_core_news_lg

if [ ! -f .env ]; then
    echo "Creating .env file from template..."
    cp .env.example .env
    echo "Please edit .env with your configuration"
fi

echo "Setup complete!"
echo "Run 'uv run python -m moderation' to start the service"
