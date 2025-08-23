package com.patbaumgartner.mcp.client.weather;

import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class McpClientWeatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpClientWeatherApplication.class, args);
	}

}

@RestController
class WeatherController {

	private final ChatClient chatClient;

	public WeatherController(ChatClient.Builder chatClientBuilder, List<McpSyncClient> mcpSyncClients) {
		this.chatClient = chatClientBuilder.defaultSystem("""
				       You are a weather assistant and you are answering only questions to weather.
				       Other questions are answered with a joke about weather and the topic.
				       Answer all questions with complete sentences.
				""")
			.defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients))
			.defaultAdvisors(MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().build()).build(),
					new SimpleLoggerAdvisor())
			.build();
	}

	@PostMapping("/ask")
	public Answer ask(@RequestBody Question question) {
		return chatClient.prompt().user(question.question()).call().entity(Answer.class);
	}

	public record Question(String question) {
	}

	public record Answer(String answer) {
	}

}