package com.fortytwotalents.openai.spring.ai;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.*;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.charset.Charset;

@SpringBootApplication
public class OpenAiImageGeneratingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenAiImageGeneratingApplication.class, args);
	}

}

@Slf4j
@Controller
@RequiredArgsConstructor
class OpenAiImageGeneratingController {

	private final ImageModel imageModel;

	@Value("classpath:custom/custom.prompt")
	private Resource prompt;

	@GetMapping("/")
	public String index() {
		return "redirect:/image";
	}

	@SneakyThrows
	@GetMapping("/image")
	public String imageGen() {

		ImageOptions options = ImageOptionsBuilder.builder()
			.model(OpenAiImageApi.ImageModel.DALL_E_3.getValue())
			.height(1024)
			.width(1024)
			.build();

		String message = StreamUtils.copyToString(prompt.getInputStream(), Charset.defaultCharset());

		ImagePrompt imagePrompt = new ImagePrompt(message, options);

		log.info("Image generation just started");
		ImageResponse response = imageModel.call(imagePrompt);

		String imageUrl = response.getResult().getOutput().getUrl();
		log.info("Image generation finished: {}", imageUrl);

		return "redirect:" + imageUrl;
	}

}