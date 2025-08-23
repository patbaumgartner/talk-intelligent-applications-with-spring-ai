package com.fortytwotalents.openai.embabel.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.config.annotation.EnableAgentShell;
import com.embabel.agent.config.annotation.EnableAgents;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.prompt.persona.Persona;
import com.embabel.common.ai.model.LlmOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Collectors;

@EnableAgentShell
@EnableAgents
@SpringBootApplication
public class OpenAiQuizEmbabelAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenAiQuizEmbabelAgentApplication.class, args);
	}

}

abstract class QuizPersonas {

	static final Persona QUIZ_WRITER = Persona.create("Quiz Writer",
			"A creative storyteller who invents exciting and entertaining quiz questions for SRF 3.",
			"Witty and imaginative", "Create questions that challenge the listeners while also being fun.");

	static final Persona QUIZ_REVIEWER = Persona.create("Quiz Reviewer",
			"A critical and attentive editor who checks every quiz question for accuracy, fairness, and clarity.",
			"Professional and precise", "Ensure that the quiz remains correct, fair, and engaging.");

	static final Persona QUIZ_HOST = Persona.create("Quiz Host",
			"A charismatic radio presenter who asks the quiz questions and interacts casually with the participants.",
			"Casual and humorous", "Turn the quiz into a lively radio experience that excites everyone.");

}

@Agent(name = "AbcSRF3",
		description = "Generate Questions where the single answer word starts with the provided letter.")
class QuizGeneratorAgent {

	private final int wordCount;

	private final LlmOptions llm;

	QuizGeneratorAgent(@Value("${abc.wordcount:20}") int wordCount, @Value("${abc.llm:gpt-5-nano}") String model) {
		this.wordCount = wordCount;
		this.llm = LlmOptions.withModel(model);
	}

	@Action
	WordsWithSameStartingLetter searchWordWithStartingLetter(UserInput input, OperationContext context) {
		return context.ai().withLlm(llm).withPromptContributor(QuizPersonas.QUIZ_WRITER).createObject("""
				Find %d words that can be used in English, starting with the letter: '%s'.
				   - Words should be easy to pronounce.
				   - Use simple, everyday vocabulary (no technical or rare words).
				""".formatted(wordCount * 2, input.getContent()), WordsWithSameStartingLetter.class);
	}

	@Action
	ReducedWordsWithSameStartingLetter filterWordsWithStartingLetter(WordsWithSameStartingLetter words,
			OperationContext context) {
		return context.ai()
			.withLlm(llm)
			.withPromptContributor(QuizPersonas.QUIZ_WRITER)
			.createObject("""
					Reduce the list to %d unique words.
					   - Avoid duplicates or very similar words.
					   - Ensure the selection is diverse, covering different everyday categories
					     (e.g. objects, food, nature, culture).

					### Words provided:
					%s
					""".formatted(wordCount,
					words.words()
						.stream()
						.map(WordWithSameStartingLetter::word)
						.distinct()
						.collect(Collectors.joining(", "))),
					ReducedWordsWithSameStartingLetter.class);
	}

	@Action
	Questions generateQuizQuestions(ReducedWordsWithSameStartingLetter words, OperationContext context) {
		return context.ai()
			.withLlm(llm)
			.withPromptContributor(QuizPersonas.QUIZ_WRITER)
			.createObject("""
					For each final word, create one simple quiz question in English.
					   - The question should be short and clear.
					   - The answer must be the word itself.
					   - Keep it common knowledge, understandable for a broad audience.

					Example: Letter 'H' – Word 'Hat':
					   "What do you wear on your head to protect it from the sun?"

					### Words provided:
					%s
					""".formatted(words.words()
				.stream()
				.map(WordWithSameStartingLetter::word)
				.distinct()
				.collect(Collectors.joining(", "))), Questions.class);
	}

	@Action
	ReviewedQuestions reviewQuizQuestions(Questions questions, OperationContext context) {
		return context.ai()
			.withLlm(llm)
			.withPromptContributor(QuizPersonas.QUIZ_REVIEWER)
			.createObject("""
					For the provided quiz questions, review and improve them:
					- Make sure the answer is correct.
					- Make each question easy to read and understand.
					- Keep the style consistent: short, clear.
					- Ensure the question is fair and unambiguous (only one obvious answer).
					- Avoid complicated words, dialect extremes, or insider knowledge.
					- Keep them entertaining but simple.

					### Questions provided:
					%s
					""".formatted(questions.questions()
				.stream()
				.map(q -> "- " + "Q: " + q.question() + " - " + "A: " + q.answer())
				.distinct()
				.collect(Collectors.joining("\n"))), ReviewedQuestions.class);
	}

	@AchievesGoal(description = "AbcSRF3 quiz has been generated")
	@Action
	PresentedQuestions presentQuizQuestions(ReviewedQuestions questions, OperationContext context) {
		return context.ai()
			.withLlm(llm)
			.withPromptContributor(QuizPersonas.QUIZ_HOST)
			.createObject("""
					Present the provided quiz questions in the style of a radio host:
					- Make a short introduction to the quiz.
					- Only in the intro you can add something to the questions.
					- The quiz runs for 45s and starts now.

					- Use English, friendly and natural (spoken style).
					- Keep each question short, clear, and energetic.
					- Add a touch of charm or humor to make it engaging.
					- Address the listeners directly when it feels natural.
					- Maintain the quiz atmosphere: exciting but not too complicated.

					### Questions provided:
					%s
					""".formatted(questions.questions()
				.stream()
				.map(q -> "- " + "Q: " + q.question() + " - " + "A: " + q.answer())
				.distinct()
				.collect(Collectors.joining("\n"))), PresentedQuestions.class);
	}

}

record ReducedWordsWithSameStartingLetter(List<WordWithSameStartingLetter> words) {
}

record WordsWithSameStartingLetter(List<WordWithSameStartingLetter> words) {
}

record WordWithSameStartingLetter(String word) {
}

record Question(String question, String answer) {
}

record Questions(List<Question> questions) {
}

record ReviewedQuestions(List<Question> questions) {
}

record PresentedQuestions(List<Question> questions) {
}