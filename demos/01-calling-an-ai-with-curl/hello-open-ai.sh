#!/usr/bin/env bash

echo "Calling OpenAI API ..."

PROMPT="Share a helpful but lesser-known tip for writing better Java code."

curl https://api.openai.com/v1/chat/completions \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $OPENAI_API_KEY" \
    --data \
"{
  \"model\": \"gpt-3.5-turbo\",
  \"messages\": [
    {
      \"role\": \"system\",
      \"content\": \"You're an experienced software engineer who gives practical advice to Java developers.\"
    },
    {
      \"role\": \"user\",
      \"content\": \"$PROMPT\"
    }
  ]
}"