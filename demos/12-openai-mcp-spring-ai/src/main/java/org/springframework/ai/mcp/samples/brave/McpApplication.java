package org.springframework.ai.mcp.samples.brave;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class McpApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpApplication.class, args);
	}

	@Bean
	public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder,
			List<McpSyncClient> mcpSyncClients) {
		return args -> {

			var chatClient = chatClientBuilder.defaultSystem(
					"You are useful assistant and can perform web searches Brave's search API to reply to your questions.")
				.defaultTools(new SyncMcpToolCallbackProvider(mcpSyncClients))
				.build();

			String question = "Does Spring AI supports the Model Context Protocol? Please provide some references.";

			System.out.println("QUESTION: " + question);
			System.out.println("ASSISTANT: " + chatClient.prompt(question).call().content());
		};
	}

	@Bean(destroyMethod = "close")
	public McpSyncClient mcpClient() {

		// https://github.com/modelcontextprotocol/servers/tree/main/src/brave-search
		var stdioParams = ServerParameters.builder("npx")
			.args("-y", "@modelcontextprotocol/server-brave-search")
			.addEnvVar("BRAVE_API_KEY", System.getenv("BRAVE_API_KEY"))
			.build();

		var mcpClient = McpClient.sync(new StdioClientTransport(stdioParams)).build();
		var init = mcpClient.initialize();

		System.out.println("MCP Initialized: " + init);

		return mcpClient;
	}

}