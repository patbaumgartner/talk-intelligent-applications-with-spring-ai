package com.fortytwotalents.openai.spring.ai;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.function.Function;

public class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

    @Override
    public Response apply(Request request) {

        double temp = 0;
        Unit unit = request.unit();

        if (request.location().contains("Munich")) {
            temp = 28;
        } else if (request.location().contains("Zurich")) {
            temp = 29;
        } else if (request.location().contains("New York")) {
            if (Unit.F.equals(unit)) {
                temp = 78;
            } else {
                temp = 17;
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
    @JsonClassDescription("Get the weather in location")
    public record Request(@JsonProperty(required = true,
            value = "location") @JsonPropertyDescription("The city and country like Zurich, Switzerland") String location,
                          @JsonProperty(required = true, value = "lat") @JsonPropertyDescription("City latitude") double lat,
                          @JsonProperty(required = true, value = "lon") @JsonPropertyDescription("City longitude") double lon,
                          @JsonProperty(required = true,
                                  value = "unit") @JsonPropertyDescription("Local unit to measure air temperature like F for Fahrenheit countries with the imperial system and C for degree Celcius in countries with metric system.") Unit unit) {
    }

    public record Response(double temp, Unit unit) {
    }

}