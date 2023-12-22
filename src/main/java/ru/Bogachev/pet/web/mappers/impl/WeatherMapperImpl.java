package ru.Bogachev.pet.web.mappers.impl;

import org.springframework.stereotype.Component;
import ru.Bogachev.pet.api.entity.Weather;
import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.web.dto.weather.WeatherDto;
import ru.Bogachev.pet.web.mappers.WeatherMapper;

import java.time.ZoneId;
import java.util.Date;

import static ru.Bogachev.pet.web.dto.weather.TimeOfDay.getTimeOfDayForTime;
import static ru.Bogachev.pet.web.dto.weather.WeatherCondition.getWeatherConditionForCode;

@Component
public class WeatherMapperImpl implements WeatherMapper {
    @Override
    public WeatherDto toDto(WeatherApiResponse weatherApiResponse) {
        if(weatherApiResponse == null) {
            return null;
        }
        Weather weather = weatherApiResponse.getWeatherList().get(0);
        return WeatherDto.builder()
                .weatherCondition(getWeatherConditionForCode(weather.getId()))
                .timeOfDay(getTimeOfDayForTime(weatherApiResponse.getDate()))
                .description(weather.getDescription())
                .temperature(weatherApiResponse.getMain().getTemperature())
                .temperatureFeelsLike(weatherApiResponse.getMain().getTemperatureFeelsLike())
                .temperatureMinimum(weatherApiResponse.getMain().getTemperatureMinimal())
                .temperatureMaximum(weatherApiResponse.getMain().getTemperatureMaximum())
                .humidity(weatherApiResponse.getMain().getHumidity())
                .pressure(weatherApiResponse.getMain().getPressure())
                .windSpeed(weatherApiResponse.getWind().getSpeed())
                .windDirection(weatherApiResponse.getWind().getDeg())
                .windGust(weatherApiResponse.getWind().getGust())
                .cloudiness(weatherApiResponse.getClouds().getCloudiness())
                .date(Date.from(weatherApiResponse.getDate().atZone(ZoneId.systemDefault()).toInstant()))
                .sunrise(Date.from(weatherApiResponse.getSys().getSunriseTime().atZone(ZoneId.systemDefault()).toInstant()))
                .sunset(Date.from(weatherApiResponse.getSys().getSunsetTime().atZone(ZoneId.systemDefault()).toInstant()))
                .build();
    }
}
