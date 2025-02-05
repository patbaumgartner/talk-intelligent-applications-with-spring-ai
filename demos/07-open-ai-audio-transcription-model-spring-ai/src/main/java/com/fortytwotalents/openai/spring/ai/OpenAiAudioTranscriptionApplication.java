package com.fortytwotalents.openai.spring.ai;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@SpringBootApplication
public class OpenAiAudioTranscriptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenAiAudioTranscriptionApplication.class, args);
	}

}

@Slf4j
@RestController
@RequiredArgsConstructor
class OpenAiAudioTranscriptionController {

	private final OpenAiAudioTranscriptionModel transcriptionModel;

	@SneakyThrows
	@GetMapping(path = "/original", produces = "audio/mpeg")
	byte[] original(@Value("classpath:dieverschwundenetasche-smaller.mp3") Resource resource) {
		return resource.getInputStream().readAllBytes();
	}

	@GetMapping("/transcription")
	String speech(@Value("classpath:dieverschwundenetasche-smaller.mp3") Resource audioFile) {
		return transcriptionModel.call(new AudioTranscriptionPrompt(audioFile)).getResult().getOutput();
	}

	@GetMapping("/transcription/provider-options")
	String speechProviderOptions(@Value("classpath:dieverschwundenetasche-smaller.mp3") Resource audioFile) {
		var transcriptionResponse = transcriptionModel.call(new AudioTranscriptionPrompt(audioFile,
				OpenAiAudioTranscriptionOptions.builder()
					.language("en")
					.prompt("Transcribe the audio file about Philipp Maloney and the missing bag.")
					.temperature(0f)
					.responseFormat(OpenAiAudioApi.TranscriptResponseFormat.VTT)
					.build()));
		return transcriptionResponse.getResult().getOutput();
	}

}
