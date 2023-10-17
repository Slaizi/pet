package ru.Bogachev.pet.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Wind {
    @JsonProperty("speed")
    private Double speed;

    @JsonProperty("deg")
    private Integer deg;

    @JsonProperty("gust")
    private Double gust;
}
