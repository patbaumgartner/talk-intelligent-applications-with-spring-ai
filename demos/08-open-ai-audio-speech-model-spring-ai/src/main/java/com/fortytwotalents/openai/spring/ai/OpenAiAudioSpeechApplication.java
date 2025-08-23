package com.fortytwotalents.openai.spring.ai;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;

@SpringBootApplication
public class OpenAiAudioSpeechApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenAiAudioSpeechApplication.class, args);
	}

}

@RestController
@RequiredArgsConstructor
class OpenAiAudioSpeechController {

	private final SpeechModel speechModel;

	@SneakyThrows
	@GetMapping(path = "/original", produces = "audio/mpeg")
	byte[] original(@Value("classpath:dieverschwundenetasche-smaller.mp3") Resource resource) {
		return resource.getInputStream().readAllBytes();
	}

	@SneakyThrows
	@GetMapping(path = "/speech", produces = "audio/mpeg")
	byte[] speech(@Value("classpath:speech-en.txt") Resource resource) {
		return speechModel.call(new SpeechPrompt(resource.getContentAsString(Charset.defaultCharset())))
			.getResult()
			.getOutput();
	}

	@SneakyThrows
	@GetMapping(path = "/speech/provider-options", produces = "audio/mpeg")
	byte[] speechProviderOptions(@Value("classpath:speech-en.txt") Resource resource) {
		var speechResponse = speechModel.call(new SpeechPrompt(resource.getContentAsString(Charset.defaultCharset()),
				OpenAiAudioSpeechOptions.builder()
					.model("tts-1")
					.voice(OpenAiAudioApi.SpeechRequest.Voice.ONYX)
					.responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
					.speed(1.3f)
					.build()));
		return speechResponse.getResult().getOutput();
	}

}
