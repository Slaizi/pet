package ru.Bogachev.pet.api.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.Bogachev.pet.api.response.LocationApiResponse;
import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.domain.entity.LocationEntity;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherApiService {
    @Value("${api.key.weather}")
    private String APP_ID;
    private static final String BASE_API_URL = "https://api.openweathermap.org";
    private static final String WEATHER_API_URL_SUFFIX = "/data/2.5/weather";
    private static final String GEOCODING_API_URL_SUFFIX = "/geo/1.0/direct";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Cacheable(cacheNames = "weatherApiResponse")
    public WeatherApiResponse getWeatherForLocation(LocationEntity location) {
        try {
            URI uri = buildUriForWeatherRequest(location);
            HttpRequest request = buildRequest(uri);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), WeatherApiResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<LocationApiResponse> getLocationByName (String nameOfLocation) {
        try {
            URI uri = buildUriForGeocodingRequest(nameOfLocation);
            HttpRequest request = buildRequest(uri);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(
                    response.body(),
                    new TypeReference<>() {
                    }
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static HttpRequest buildRequest(URI uri) {
        return HttpRequest.newBuilder(uri)
                          .GET()
                          .build();
    }
    private URI buildUriForWeatherRequest(LocationEntity location) {
        return URI.create(BASE_API_URL + WEATHER_API_URL_SUFFIX
                + "?lat=" + location.getLatitude()
                + "&lon=" + location.getLongitude()
                + "&appid=" + APP_ID
                + "&units=" + "metric");
    }
    private URI buildUriForGeocodingRequest(String nameOfLocation) {
        return URI.create(BASE_API_URL + GEOCODING_API_URL_SUFFIX
                + "?q=" + nameOfLocation
                + "&limit=5"
                + "&appid=" + APP_ID);
    }
}
