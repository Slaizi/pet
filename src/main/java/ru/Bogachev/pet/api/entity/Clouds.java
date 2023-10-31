package ru.Bogachev.pet.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class Clouds implements Serializable {
    @JsonProperty("all")
    private Integer cloudiness;
}
