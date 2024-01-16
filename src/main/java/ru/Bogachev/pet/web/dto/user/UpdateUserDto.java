package ru.Bogachev.pet.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class UpdateUserDto {

    private Long id;

    @NotNull(
            message = "Email cannot be empty."
    )
    @Email(
            message = "Email is not correct"
    )
    @Length(
            max = 255,
            message = "Email length must be smaller than 255 symbols."
    )
    private String username;

    private List<String> roles;

}
