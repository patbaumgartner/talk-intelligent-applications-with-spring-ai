#!/usr/bin/env bash

echo "Calling Google Gemini API ..."

PROMPT="Share a helpful but lesser-known tip for writing better Java code."

curl -X POST "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=$GOOGLE_API_KEY" \
    -H "Content-Type: application/json" \
    --data \
"{
    \"contents\":[
        {\"parts\":[
            {\"text\":\"$PROMPT\"}
    ]}
]}"
