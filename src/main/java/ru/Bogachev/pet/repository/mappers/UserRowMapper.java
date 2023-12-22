package ru.Bogachev.pet.repository.mappers;

import lombok.SneakyThrows;
import ru.Bogachev.pet.domain.location.Location;
import ru.Bogachev.pet.domain.user.Role;
import ru.Bogachev.pet.domain.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserRowMapper {
    @SneakyThrows
    public static User mapRow (ResultSet resultSet) {
        Set<Role> roles = new HashSet<>();
        while (resultSet.next()) {
            roles.add(Role.valueOf(resultSet.getString("user_role_role")));
        }
        resultSet.beforeFirst();

        List<Location> locations = LocationRowMapper.mapRows(resultSet);
        resultSet.beforeFirst();

        if(resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getLong("user_id"));
            user.setUsername(resultSet.getString("user_username"));
            user.setPassword(resultSet.getString("user_password"));
            user.setRoles(roles);
            user.setLocations(locations);
            return user;
        }
        return null;
    }
    @SneakyThrows
    public static List<User> mapRows(ResultSet resultSet) {
        Map<Long, User> userMap = new HashMap<>();
        List<Location> locations = LocationRowMapper.mapRows(resultSet);
        resultSet.beforeFirst();

        while (resultSet.next()) {
            long userId = resultSet.getLong("user_id");
            User user = userMap.computeIfAbsent(userId, id -> {
                User newUser = new User();
                newUser.setId(id);
                try {
                    newUser.setUsername(resultSet.getString("user_username"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    newUser.setPassword(resultSet.getString("user_password"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                newUser.setRoles(new HashSet<>());
                newUser.setLocations(locations);
                return newUser;
            });

            String role = resultSet.getString("user_role_role");
            if (role != null) {
                user.getRoles().add(Role.valueOf(role));
            }
        }

        return new ArrayList<>(userMap.values());
    }
}
