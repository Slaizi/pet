package ru.Bogachev.pet.domain.user;

import jakarta.persistence.*;
import lombok.Data;
import ru.Bogachev.pet.domain.location.Location;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_locations",
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    private List<Location> locations;

    @CollectionTable(name = "users_roles")
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

}
