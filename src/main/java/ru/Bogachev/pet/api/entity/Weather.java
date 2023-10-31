package ru.Bogachev.pet.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather implements Serializable {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("main")
    private String currentState;

    @JsonProperty("description")
    private String description;
}
