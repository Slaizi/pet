package ru.Bogachev.pet.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.Bogachev.pet.api.response.LocationApiResponse;
import ru.Bogachev.pet.api.response.WeatherApiResponse;
import ru.Bogachev.pet.domain.location.Location;
import ru.Bogachev.pet.service.WeatherApiService;
import ru.Bogachev.pet.service.props.WeatherProperties;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherApiServiceImpl implements WeatherApiService {

    private final WeatherProperties weatherProperties;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public WeatherApiResponse getWeatherForLocation(Location location) {
        return bodyGetWeatherLocation(location);
    }

    private WeatherApiResponse bodyGetWeatherLocation(Location location) {
        try {
            URI uri = buildUriForWeatherRequest(location);
            HttpRequest request = buildRequest(uri);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), WeatherApiResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LocationApiResponse> getLocationByName(String nameOfLocation) {
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

    private URI buildUriForWeatherRequest(Location location) {
        return URI.create(weatherProperties.getBasicPath() + weatherProperties.getWeatherSuffix()
                          + "?lat=" + location.getLatitude()
                          + "&lon=" + location.getLongitude()
                          + "&appid=" + weatherProperties.getApiKey()
                          + "&units=" + "metric");
    }

    private URI buildUriForGeocodingRequest(String nameOfLocation) {
        return URI.create(weatherProperties.getBasicPath() + weatherProperties.getGeocodingSuffix()
                          + "?q=" + nameOfLocation
                          + "&limit=5"
                          + "&appid=" + weatherProperties.getApiKey());
    }
}
