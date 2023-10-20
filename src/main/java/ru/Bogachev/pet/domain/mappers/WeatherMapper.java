package ru.Bogachev.pet.domain.mappers;

import org.mapstruct.Mapper;
import ru.Bogachev.pet.api.entity.Weather;
import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.domain.dto.WeatherDto;

import java.time.ZoneId;
import java.util.Date;

import static ru.Bogachev.pet.domain.entity.enums.TimeOfDay.getTimeOfDayForTime;
import static ru.Bogachev.pet.domain.entity.enums.WeatherCondition.getWeatherConditionForCode;

@Mapper(componentModel = "spring")
public interface WeatherMapper {
    default WeatherDto toDto(WeatherApiResponse weatherApiResponse) {
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
