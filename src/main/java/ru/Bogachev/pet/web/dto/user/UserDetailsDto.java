package ru.Bogachev.pet.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import ru.Bogachev.pet.domain.user.Role;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDetailsDto {

    private Long id;

    @NotNull(message = "Email cannot be empty.")
    @Email(message = "Email is not correct")
    @Length(max = 255, message = "Email length must be smaller than 255 symbols.")
    private String username;

    @NotNull(message = "Password cannot be empty.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public Set<Role> getRoles() {
        return this.authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}
