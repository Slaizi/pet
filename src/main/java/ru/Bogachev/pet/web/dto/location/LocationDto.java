package ru.Bogachev.pet.web.dto.location;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LocationDto {

    @NotNull(message = "Id must be not null.")
    private Long id;

    @NotNull(message = "Location name must be not null.")
    @Length(max = 255, message = "Location name length must be smaller than 255 symbols.")
    private String name;

    @NotNull(message = "Latitude must be not null.")
    private Double latitude;

    @NotNull(message = "Longitude must be not null.")
    private Double longitude;

}
