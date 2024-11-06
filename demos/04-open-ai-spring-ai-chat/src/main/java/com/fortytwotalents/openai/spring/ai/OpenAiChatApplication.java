package com.fortytwotalents.openai.spring.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class OpenAiChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenAiChatApplication.class, args);
    }

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean
    CommandLineRunner commandLineRunner(ChatClient chatClient) {
        return args -> {

            Joke joke = chatClient.prompt()
                    .system("You are a friendly chatbot and you like to place emojis everywhere.")
                    .user("Tell me a developer joke about Python.")
                    .call()
                    .entity(Joke.class);

            log.info("Joke - Prompt: {} - Punchline: {}", joke.prompt(), joke.punchline());
        };
    }

    record Joke(String prompt, String punchline) {
    }

}
