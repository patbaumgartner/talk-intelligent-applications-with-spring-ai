#!/usr/bin/env bash

echo "Calling Anthropic Claude API ..."

PROMPT="Share a helpful but lesser-known tip for writing better Java code."

curl https://api.anthropic.com/v1/messages \
    --header "x-api-key: $ANTHROPIC_API_KEY" \
    --header "anthropic-version: 2023-06-01" \
    --header "content-type: application/json" \
    --data \
"{
    \"model\": \"claude-opus-4-1-20250805\",
    \"max_tokens\": 1024,
    \"messages\": [
        {\"role\": \"user\", \"content\": \"$PROMPT\"}
    ]
}"