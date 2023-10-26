package ru.Bogachev.pet.domain.entity.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class WeatherConditionTest {
    @Test
    public void testGetWeatherConditionForCode_Thunderstorm() {
        WeatherCondition thunderstorm = WeatherCondition.getWeatherConditionForCode(200);
        WeatherCondition drizzle = WeatherCondition.getWeatherConditionForCode(300);
        WeatherCondition rain = WeatherCondition.getWeatherConditionForCode(500);
        WeatherCondition snow = WeatherCondition.getWeatherConditionForCode(600);
        WeatherCondition atmosphere = WeatherCondition.getWeatherConditionForCode(700);
        WeatherCondition clear = WeatherCondition.getWeatherConditionForCode(800);
        WeatherCondition cloud = WeatherCondition.getWeatherConditionForCode(802);
        WeatherCondition undefined = WeatherCondition.getWeatherConditionForCode(1000);

        assertEquals(WeatherCondition.THUNDERSTORM, thunderstorm);
        assertEquals(WeatherCondition.DRIZZLE, drizzle);
        assertEquals(WeatherCondition.RAIN, rain);
        assertEquals(WeatherCondition.SNOW, snow);
        assertEquals(WeatherCondition.ATMOSPHERE, atmosphere);
        assertEquals(WeatherCondition.CLEAR, clear);
        assertEquals(WeatherCondition.CLOUDS, cloud);
        assertEquals(WeatherCondition.UNDEFINED, undefined);

    }
}
