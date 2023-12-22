package ru.Bogachev.pet.domain.location;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class Location implements Serializable {

    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;

}
