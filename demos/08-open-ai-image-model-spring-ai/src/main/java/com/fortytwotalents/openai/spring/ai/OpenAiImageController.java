package com.fortytwotalents.openai.spring.ai;

import java.nio.charset.Charset;

import org.springframework.ai.image.*;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OpenAiImageController {

	@Value("classpath:custom/custom.prompt")
	private Resource prompt;

	private final ImageModel imageModel;

	@SneakyThrows
	@GetMapping("/image")
	public String imageGen() {

		ImageOptions options = ImageOptionsBuilder.builder()
			.withModel(OpenAiImageApi.ImageModel.DALL_E_3.getValue())
			.withHeight(1024)
			.withWidth(1024)
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
