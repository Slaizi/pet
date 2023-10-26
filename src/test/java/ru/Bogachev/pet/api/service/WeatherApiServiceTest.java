package ru.Bogachev.pet.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import ru.Bogachev.pet.domain.entity.LocationEntity;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class WeatherApiServiceTest {
    @Value("${api.key.weather}")
    private String APP_ID;
    private static final String BASE_API_URL = "https://api.openweathermap.org";
    private static final String WEATHER_API_URL_SUFFIX = "/data/2.5/weather";
    private static final String GEOCODING_API_URL_SUFFIX = "/geo/1.0/direct";
    @Mock
    private WeatherApiService weatherApiService;
    @Test
    void buildUriForWeatherRequestTest() throws Exception {
        LocationEntity location = new LocationEntity();
        location.setLatitude(40.7128);
        location.setLongitude(-74.0060);

        String expectedUrl = BASE_API_URL + WEATHER_API_URL_SUFFIX +
                "?lat=40.7128&" +
                "lon=-74.006&" +
                "appid=" + APP_ID +
                "&units=metric";
        URI expectedUri = URI.create(expectedUrl);
        Method privateMethod = WeatherApiService.class.getDeclaredMethod("buildUriForWeatherRequest", LocationEntity.class);
        privateMethod.setAccessible(true);

        URI actualUri = (URI) privateMethod.invoke(weatherApiService, location);
        assertNotNull(actualUri);
        assertEquals(expectedUri, actualUri);
    }
    @Test
    void buildUriForGeocodingRequestTest() throws Exception {
        String nameOfLocation = "London";
        var expectedUri = URI.create(BASE_API_URL + GEOCODING_API_URL_SUFFIX
                + "?q=" + nameOfLocation
                + "&limit=5"
                + "&appid=" + APP_ID);
        Method privateMethod = WeatherApiService.class.getDeclaredMethod("buildUriForGeocodingRequest", String.class);
        privateMethod.setAccessible(true);

        URI actualUri = (URI) privateMethod.invoke(weatherApiService, nameOfLocation);
        assertNotNull(actualUri);
        assertEquals(expectedUri, actualUri);
    }
    @Test
    void buildRequestTest() throws Exception {
        URI uri = new URI("http://example.com");
        Method privateMethod = WeatherApiService.class.getDeclaredMethod("buildRequest", URI.class);
        privateMethod.setAccessible(true);
        HttpRequest request = (HttpRequest) privateMethod.invoke(weatherApiService, uri);

        assertEquals(uri, request.uri());
        assertEquals("GET", request.method());
    }
}
