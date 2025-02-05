# Calling Ollama from Spring AI

## Exploring Ollama as a Local Environment

1. To run Ollama with a local model you can download and install the desktop application from <https://ollama.com/> or you can use the provided `docker-compose.yml` to run everything including the Open WebUI inside Docker.
2. Once installed, you will need to download a model.

- In a terminal run `ollama run {put your model name here}`.
- Or in the web interface go to `localhost:8080` to see the ChatGPT like frontend where you can also download a model.
- Choose a model like `llama3.1` from <https://ollama.com/library>.

3. For configuration see the [Spring AI documentation on Ollama chat](https://docs.spring.io/spring-ai/reference/api/chat/ollama-chat.html)