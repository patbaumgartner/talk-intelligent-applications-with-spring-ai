package com.fortytwotalents.openai.spring.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@Slf4j
@SpringBootApplication
public class OpenAiFunctionCallingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenAiFunctionCallingApplication.class, args);
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
				.toolNames("weatherFunction")
				.call()
				.content();

			log.info("ChatGPT answered: {}", answer);
		};
	}

}
