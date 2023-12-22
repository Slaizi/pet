package ru.Bogachev.pet.web.dto.weather;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class WeatherDto implements Serializable {
    private WeatherCondition weatherCondition;

    private TimeOfDay timeOfDay;

    private String description;

    private Double temperature;

    private Double temperatureFeelsLike;

    private Double temperatureMinimum;

    private Double temperatureMaximum;

    private Integer humidity;

    private Integer pressure;

    private Double windSpeed;

    private Integer windDirection;

    private Double windGust;

    private Integer cloudiness;

    private Date date;

    private Date sunrise;

    private Date sunset;
}
