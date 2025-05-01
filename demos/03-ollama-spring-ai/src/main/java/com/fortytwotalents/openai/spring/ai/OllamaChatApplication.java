package com.fortytwotalents.openai.spring.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class OllamaChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(OllamaChatApplication.class, args);
	}

	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		return builder.build();
	}

	@Bean
	CommandLineRunner commandLineRunner(ChatClient chatClient) {
		return args -> {

			String answer = chatClient.prompt()
				.system("You're an experienced software engineer who gives practical advice to Java developers.")
				.user("Share a helpful but lesser-known tip for writing better Java code.")
				.call()
				.content();

			log.info("Answer: {}", answer);
		};
	}

}
