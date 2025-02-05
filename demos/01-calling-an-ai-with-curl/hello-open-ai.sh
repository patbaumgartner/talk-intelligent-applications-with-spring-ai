#!/usr/bin/env bash

echo "Calling OpenAI API ..."

PROMPT="Tell me a friendly developer joke about JAVA."

curl https://api.openai.com/v1/chat/completions \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $SPRING_AI_OPENAI_API_KEY" \
    --data \
"{
  \"model\": \"gpt-3.5-turbo\",
  \"messages\": [
    {
      \"role\": \"system\",
      \"content\": \"You are a friendly chatbot and you like to place emojis everywhere.\"
    },
    {
      \"role\": \"user\",
      \"content\": \"$PROMPT\"
    }
  ]
}"