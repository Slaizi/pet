package ru.Bogachev.pet.config;

import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.Bogachev.pet.repository.LocationRepository;
import ru.Bogachev.pet.repository.UserRepository;
import ru.Bogachev.pet.service.LocationService;
import ru.Bogachev.pet.service.UserService;
import ru.Bogachev.pet.service.WeatherApiService;
import ru.Bogachev.pet.service.impl.LocationServiceImpl;
import ru.Bogachev.pet.service.impl.UserServiceImpl;
import ru.Bogachev.pet.service.impl.WeatherApiServiceImpl;
import ru.Bogachev.pet.service.props.WeatherProperties;
import ru.Bogachev.pet.web.mappers.LocationMapper;
import ru.Bogachev.pet.web.mappers.LocationMapperImpl;
import ru.Bogachev.pet.web.mappers.WeatherMapper;
import ru.Bogachev.pet.web.mappers.WeatherMapperImpl;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {
    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }
    @Bean
    @Primary
    public UserService userService() {
        return new UserServiceImpl(userRepository(), testPasswordEncoder());
    }

    @Bean
    public LocationRepository locationRepository() {
        return Mockito.mock(LocationRepository.class);
    }
    @Bean
    public WeatherProperties weatherProperties() {
        WeatherProperties weatherProperties = new WeatherProperties();
        weatherProperties.setApiKey("dmdqYmhqbmttYmNhamNjZWhxa25hd2puY2xhZWtic3ZlaGtzYmJ1dg");
        return weatherProperties;
    }
    @Bean
    @Primary
    public WeatherApiService weatherApiService() {
        return new WeatherApiServiceImpl(weatherProperties());
    }
    @Bean
    @Primary
    public WeatherMapper weatherMapper() {
        return new WeatherMapperImpl();
    }
    @Bean
    @Primary
    public LocationMapper locationMapper() {
        return new LocationMapperImpl();
    }
    @Bean
    @Primary
    public LocationService locationService() {
        return new LocationServiceImpl(
                locationRepository(),
                userService(),
                weatherApiService(),
                weatherMapper(),
                locationMapper()
                );
    }
}
