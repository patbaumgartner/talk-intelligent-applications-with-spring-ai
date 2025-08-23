package com.fortytwotalents.openai.spring.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.function.Function;

@Slf4j
@SpringBootApplication
public class OpenAiFunctionCallingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenAiFunctionCallingApplication.class, args);
	}

	@Bean
	Function<WeatherService.Request, WeatherService.Response> weatherFunction() {
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

class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

	@Override
	@Tool(description = "Get the weather in location")
	public Response apply(Request request) {

		double temp = 0;
		Unit unit = request.unit();

		if (request.location().contains("Munich")) {
			temp = 6;
		}
		else if (request.location().contains("Zurich")) {
			temp = 10;
		}
		else if (request.location().contains("New York")) {
			if (Unit.F.equals(unit)) {
				temp = 31;
			}
			else {
				temp = -1;
			}
		}

		return new Response(temp, unit);
	}

	public enum Unit {

		C("Celsius"),

		F("Fahrenheit");

		public final String unitName;

		Unit(String text) {
			this.unitName = text;
		}

	}

	@JsonInclude(Include.NON_NULL)
	public record Request(@JsonProperty(required = true,
			value = "location") @JsonPropertyDescription("Location: Provide the location (city, country) to retrieve temperature data for the desired area.") String location,
			@JsonProperty(required = true, value = "lat") @JsonPropertyDescription("City latitude") double lat,
			@JsonProperty(required = true, value = "lon") @JsonPropertyDescription("City longitude") double lon,
			@JsonProperty(required = true,
					value = "unit") @JsonPropertyDescription("Unit of temperature: Specify the unit of temperature to be used in the API request. Use 'F' for Fahrenheit in countries using the imperial system (e.g., United States), and 'C' for Celsius in countries using the metric system (e.g., most of Europe, Asia, etc.).") Unit unit) {
	}

	public record Response(double temp, Unit unit) {
	}

}
