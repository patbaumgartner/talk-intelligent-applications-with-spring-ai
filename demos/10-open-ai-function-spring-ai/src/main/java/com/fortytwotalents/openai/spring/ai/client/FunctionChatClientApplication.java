package com.fortytwotalents.openai.spring.ai.client;

import com.fortytwotalents.openai.spring.ai.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@Slf4j
@SpringBootApplication
public class FunctionChatClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunctionChatClientApplication.class, args);
	}

	@Bean
	public Function<WeatherService.Request, WeatherService.Response> weatherFunction() {
		return new WeatherService();
	}

	@Bean
	CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
		ChatClient chatClient = chatClientBuilder.build();

		return args -> {

			String answer = chatClient.prompt()
				.user("What's the weather like in Munich, Zurich, and New York?")
				.functions("weatherFunction")
				.call()
				.content();

			log.info("ChatGPT answered: {}", answer);
		};
	}

}