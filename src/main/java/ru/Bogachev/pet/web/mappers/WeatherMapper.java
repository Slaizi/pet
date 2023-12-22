package ru.Bogachev.pet.web.mappers;

import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.web.dto.weather.WeatherDto;


public interface WeatherMapper {
    WeatherDto toDto(WeatherApiResponse weatherApiResponse);
}
