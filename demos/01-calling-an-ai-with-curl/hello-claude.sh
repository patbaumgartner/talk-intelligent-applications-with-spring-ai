#!/usr/bin/env bash

PROMPT="Share a helpful but lesser-known tip for writing better Java code."

PROMPT="Tell me a developer joke about Python."

curl https://api.anthropic.com/v1/messages \
    --header "x-api-key: $ANTHROPIC_API_KEY" \
    --header "anthropic-version: 2023-06-01" \
    --header "content-type: application/json" \
    --data \
"{
    \"model\": \"claude-3-opus-20240229\",
    \"max_tokens\": 1024,
    \"messages\": [
        {\"role\": \"user\", \"content\": \"$PROMPT\"}
    ]
}"