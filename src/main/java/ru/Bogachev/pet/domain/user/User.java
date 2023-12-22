package ru.Bogachev.pet.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.Bogachev.pet.domain.location.Location;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Data
public class User {

    private Long id;
    private String username;
    private String password;
    private List<Location> locations;
    private Set<Role> roles;

}
