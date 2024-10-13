package com.fortytwotalents.openai.spring.ai.simplevector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
public class RagChatModelApplication {

	@Value("Artificial intelligence - Wikipedia.pdf")
	private Resource pdf;

	public static void main(String[] args) {
		SpringApplication.run(RagChatModelApplication.class, args);
	}

	@Bean
	VectorStore vectors(EmbeddingModel embeddingModel) {

		SimpleVectorStore vectors = new SimpleVectorStore(embeddingModel);

		PagePdfDocumentReader reader = new PagePdfDocumentReader(pdf);
		TokenTextSplitter splitter = new TokenTextSplitter();

		List<Document> documents = splitter.apply(reader.get());
		vectors.accept(documents);

		return vectors;
	}

	@Bean
	CommandLineRunner commandLineRunner(ChatModel chatModel, VectorStore vectors) {

		return args -> {
			String userPrompt = "Why is the definition of AI difficult?";

			List<Document> documents = vectors.similaritySearch(userPrompt);
			String inlined = documents.stream()
				.map(Document::getContent)
				.collect(Collectors.joining(System.lineSeparator()));

			String systemPrompt = """
					You are a virtual assistant.
					You answer questions with data provided in the DOCUMENTS section.
					You are only allowed to use information from the DOCUMENTS paragraph and no other information.
					If you are not sure or don't know, honestly say you don't know.

					DOCUMENTS:
					{documents}
					""";

			Message system = new SystemPromptTemplate(systemPrompt).createMessage(Map.of("documents", inlined));
			UserMessage user = new UserMessage(userPrompt);

			Prompt combinedPrompt = new Prompt(List.of(system, user));
			String answer = chatModel.call(combinedPrompt).getResult().getOutput().getContent();

			log.info("ChatGPT answered: {}", answer);
		};
	}

}
