package ru.Bogachev.pet.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import ru.Bogachev.pet.api.entity.Clouds;
import ru.Bogachev.pet.api.entity.Main;
import ru.Bogachev.pet.api.entity.Weather;
import ru.Bogachev.pet.api.entity.Wind;
import ru.Bogachev.pet.api.response.utils.UnixTimestampDeserializer;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherApiResponse {
    @JsonProperty("weather")
    private List<Weather> weatherList;

    @JsonProperty("main")
    private Main main;

    @JsonProperty("wind")
    private Wind wind;

    @JsonProperty("clouds")
    private Clouds clouds;

    @JsonProperty("dt")
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private LocalDateTime date;

    @JsonProperty("sys")
    private Sys sys;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sys {
        @JsonProperty("sunrise")
        @JsonDeserialize(using = UnixTimestampDeserializer.class)
        private LocalDateTime sunriseTime;

        @JsonProperty("sunset")
        @JsonDeserialize(using = UnixTimestampDeserializer.class)
        private LocalDateTime sunsetTime;
    }
}
