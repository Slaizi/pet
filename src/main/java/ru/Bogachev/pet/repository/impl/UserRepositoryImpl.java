package ru.Bogachev.pet.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import ru.Bogachev.pet.domain.exception.ResourceMappingException;
import ru.Bogachev.pet.domain.exception.ResourceNotFoundException;
import ru.Bogachev.pet.domain.user.Role;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.repository.DataSourceConfig;
import ru.Bogachev.pet.repository.UserRepository;
import ru.Bogachev.pet.repository.mappers.UserRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSourceConfig dataSourceConfig;

    private static final String FIND_BY_ID = """
            SELECT
                u.id AS user_id,
                u.username AS user_username,
                u.password AS user_password,
                ur.role AS user_role_role,
                l.id AS location_id,
                l.name AS location_name,
                l.latitude AS location_latitude,
                l.longitude AS location_longitude
            FROM
                users u
            LEFT JOIN
                users_roles ur
                    ON u.id = ur.user_id
            LEFT JOIN
                users_locations ul
                    ON u.id = ul.user_id
            LEFT JOIN
                locations l
                    ON ul.location_id = l.id
            WHERE
                u.id = ?;
            """;
    private static final String FIND_ALL = """
            SELECT
                u.id AS user_id,
                u.username AS user_username,
                u.password AS user_password,
                ur.role AS user_role_role,
                l.id AS location_id,
                l.name AS location_name,
                l.latitude AS location_latitude,
                l.longitude AS location_longitude
            FROM
                users u
            LEFT JOIN
                users_roles ur
                    ON u.id = ur.user_id
            LEFT JOIN
                users_locations ul
                    ON u.id = ul.user_id
            LEFT JOIN
                locations l
                    ON ul.location_id = l.id
            """;
    private static final String FIND_ALL_USERS_BY_LOCATION_ID = """
            SELECT
                  u.id AS user_id,
                  u.username AS user_username,
                  u.password AS user_password,
                  ur.role AS user_role_role,
                  l.id AS location_id,
                  l.name AS location_name,
                  l.latitude AS location_latitude,
                  l.longitude AS location_longitude
            FROM
                 users u
            JOIN
                users_locations ul ON u.id = ul.user_id
            JOIN
                 users_roles ur ON u.id = ur.user_id
            JOIN
                locations l ON ul.location_id = l.id
            WHERE
                 l.id = ?;
            """;
    private static final String FIND_BY_USERNAME = """
            SELECT
                u.id AS user_id,
                u.username AS user_username,
                u.password AS user_password,
                ur.role AS user_role_role,
                l.id AS location_id,
                l.name AS location_name,
                l.latitude AS location_latitude,
                l.longitude AS location_longitude
            FROM
                users u
            LEFT JOIN
                users_roles ur
                    ON u.id = ur.user_id
            LEFT JOIN
                users_locations ul
                    ON u.id = ul.user_id
            LEFT JOIN
                locations l
                    ON ul.location_id = l.id
            WHERE
                u.username = ?;
            """;
    private static final String UPDATE = """
            UPDATE users
            SET username = ?,
                password = ?
            WHERE id = ?""";
    private static final String DELETE_USER_ROLES_BY_USER_ID = """
            DELETE FROM users_roles
            WHERE user_id = ?;
            """;
    private static final String SAVE = """
            INSERT INTO users (username, password)
            VALUES (?, ?);
            """;
    private static final String SAVE_USER_ROLE = """
            INSERT INTO users_roles (user_id, role)
            VALUES (?, ?);
            """;
    private static final String DELETE = """
            DELETE
            FROM users
            WHERE
                 id = ?
            """;
    @Override
    public Optional<User> findById(Long id) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_ID,
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            statement.setLong(1, id);
            try(ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new ResourceNotFoundException("Exception while finding user by id.");
        }
    }

    @Override
    public List<User> findAll() {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(FIND_ALL,
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            try(ResultSet rs = statement.executeQuery()) {
                return UserRowMapper.mapRows(rs);
            }

        } catch (SQLException e) {
            throw new ResourceNotFoundException("Exception while finding all users.");
        }
    }

    @Override
    public void update(User user) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(UPDATE)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setLong(3, user.getId());
            statement.executeUpdate();

            saveUserRoles(user);

        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while updating user.");
        }
    }

    @Override
    public List<User> findAllUsersByLocationId(Long locationId) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(FIND_ALL_USERS_BY_LOCATION_ID,
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            statement.setLong(1, locationId);
            try(ResultSet rs = statement.executeQuery()) {
                return UserRowMapper.mapRows(rs);
            }

        } catch (SQLException e) {
            throw new ResourceNotFoundException("Exception while finding all users by location id.");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_USERNAME,
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            statement.setString(1, username);
            try(ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new ResourceNotFoundException("Exception while finding user by username.");
        }
    }

    @Override
    public void save(User user) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement1 = connection.prepareStatement(SAVE, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement1.setString(1, user.getUsername());
            statement1.setString(2, user.getPassword());
            statement1.executeUpdate();
                try (ResultSet rs = statement1.getGeneratedKeys()) {
                    rs.next();
                    long userId = rs.getLong(1);
                    user.setId(userId);
                }

            saveUserRoles(user);

        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while creating user.");
        }
    }

    private void saveUserRoles(User user) {
        deleteUserRolesByUserId(user.getId());
        Set<Role> roles = user.getRoles();
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(SAVE_USER_ROLE)) {

            for (Role role : roles) {
                statement.setLong(1, user.getId());
                statement.setString(2, role.name());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while saving user roles.");
        }
    }
    private void deleteUserRolesByUserId(Long id) {
        try(Connection connection = dataSourceConfig.getConnection();
            var statement = connection.prepareStatement(DELETE_USER_ROLES_BY_USER_ID)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while deleting user roles.");
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(DELETE)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while deleting user.");
        }
    }
}
