package com.fortytwotalents.openai.spring.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;

@Slf4j
@SpringBootApplication
public class OpenAiImageReadingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenAiImageReadingApplication.class, args);
	}

	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		return builder.build();
	}

	@Bean
	CommandLineRunner commandLineRunner(ChatClient chatClient,
			@Value("classpath:mainz-weather.png") Resource imageResourceWeather) {
		return args -> {

			String answer = chatClient.prompt()
				.user(userSpec -> userSpec.text("What will be the weather like on Thuesday?")
					.media(MimeTypeUtils.IMAGE_PNG, imageResourceWeather))
				.call()
				.content();

			log.info("Answer: {}", answer);
		};
	}

}
