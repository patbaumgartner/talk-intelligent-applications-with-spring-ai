package com.fortytwotalents.openai.spring.ai.pgvector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.util.List;

@Slf4j
@SpringBootApplication
public class OpenAiRagApplication {

	@Value("Artificial intelligence - Wikipedia.pdf")
	private Resource pdf;

	public static void main(String[] args) {
		SpringApplication.run(OpenAiRagApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
		ChatClient chatClient = chatClientBuilder.build();

		return args -> {
			PagePdfDocumentReader reader = new PagePdfDocumentReader(pdf);
			TokenTextSplitter splitter = new TokenTextSplitter();
			List<Document> documents = splitter.apply(reader.get());
			vectorStore.accept(documents);

			String answer = chatClient.prompt()
				.system("""
						You are a virtual assistant and answers questions with the data provided.
						If you are not sure or don't know, honestly say you don't know.
						""")
				.user("Why is the definition of AI difficult?")
				.advisors(new QuestionAnswerAdvisor(vectorStore))
				.call()
				.content();

			log.info("ChatGPT answered: {}", answer);
		};
	}

}
