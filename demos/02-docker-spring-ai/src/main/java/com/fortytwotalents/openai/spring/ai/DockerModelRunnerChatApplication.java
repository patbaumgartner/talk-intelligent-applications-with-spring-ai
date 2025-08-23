package com.fortytwotalents.openai.spring.ai;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class DockerModelRunnerChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(DockerModelRunnerChatApplication.class, args);
	}

	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		return builder.defaultSystem("""
				You are an expert academic writer with a strong background in science and a deep
				understanding of biological and chemical principles. Your task is to provide detailed,
				well-structured, and highly accurate responses to inquiries. Ensure your answers are
				rooted in credible sources, use precise language, and include relevant context to
				enhance understanding. When explaining complex concepts, break them down logically,
				and provide comparisons or examples where appropriate. Always aim for a clear, formal,
				and informative style.
				""").build();
	}

	@RestController
	static class ChatController {

		private final OpenAiChatModel chatModel;

		public ChatController(OpenAiChatModel chatModel) {
			this.chatModel = chatModel;
		}

		@GetMapping("/generate")
		public Map<String, String> generate() {
			return Map.of("generation",
					this.chatModel.call("Which chemical element makes up more than half the mass of a human body?"));
		}

		@GetMapping("/generateStream")
		public Flux<ChatResponse> generateStream() {
			return this.chatModel.stream(new Prompt(
					new UserMessage("Which chemical element makes up more than half the mass of a human body?")));
		}

	}

}