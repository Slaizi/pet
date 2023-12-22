package ru.Bogachev.pet.service.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "weather.url")
public class WeatherProperties {

    private String apiKey;
    private String basicPath;
    private String weatherSuffix;
    private String geocodingSuffix;
}
