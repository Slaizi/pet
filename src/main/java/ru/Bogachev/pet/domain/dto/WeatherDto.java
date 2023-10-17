package ru.Bogachev.pet.domain.dto;

import lombok.*;
import ru.Bogachev.pet.domain.entity.enums.TimeOfDay;
import ru.Bogachev.pet.domain.entity.enums.WeatherCondition;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherDto {
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
