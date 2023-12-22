package ru.Bogachev.pet.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.Bogachev.pet.domain.exception.ResourceMappingException;
import ru.Bogachev.pet.domain.exception.ResourceNotFoundException;
import ru.Bogachev.pet.domain.location.Location;
import ru.Bogachev.pet.domain.user.User;
import ru.Bogachev.pet.repository.DataSourceConfig;
import ru.Bogachev.pet.repository.LocationRepository;
import ru.Bogachev.pet.repository.mappers.LocationRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {

    private final DataSourceConfig dataSourceConfig;
    private static final String FIND_BY_ID = """
            SELECT
               l.id AS location_id,
               l.name AS location_name,
               l.latitude AS location_latitude,
               l.longitude AS location_longitude
            FROM locations l
            WHERE id = ?;
            """;
    private static final String FIND_BY_NAME = """
            SELECT
               l.id AS location_id,
               l.name AS location_name,
               l.latitude AS location_latitude,
               l.longitude AS location_longitude
            FROM locations l
            WHERE name = ?;
            """;
    private static final String FIND_ALL_LOCATIONS_BY_USER_ID = """
            SELECT
                l.id AS location_id,
                l.name AS location_name,
                l.latitude AS location_latitude,
                l.longitude AS location_longitude
            FROM
                locations l
            JOIN
                users_locations ul
                    ON l.id = ul.location_id
            WHERE
                ul.user_id = ?;
            """;
    private static final String FIND_BY_USER_ID_AND_LOCATION_NAME = """
            SELECT
                l.id AS location_id,
                l.name AS location_name,
                l.latitude AS location_latitude,
                l.longitude AS location_longitude
            FROM
                locations l
            JOIN
                users_locations ul
                    ON l.id = ul.location_id
            WHERE
                ul.user_id = ?
            AND l.name = ?;
            """;
    private static final String SAVE = """
            INSERT
            INTO locations
            (
              name,
              latitude,
              longitude
            )
            values
                (?, ?, ?);
            """;
    private static final String SAVE_USERS_LOCATIONS = """
            INSERT
            INTO users_locations
                (
                user_id,
                location_id
                )
            values
                (?, ?);
            """;
    private static final String DELETE = """
            DELETE
            FROM locations
            WHERE
                 id = ?
            """;
    private static final String DELETE_USERS_LOCATIONS_WHERE_USER_ID_AND_LOCATION_ID = """
            DELETE
            FROM
                users_locations ul
            WHERE
                ul.user_id = ?
            AND
               ul.location_id = ?;
            """;
    @Override
    public Optional<Location> findById(Long id) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_ID,
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            statement.setLong(1, id);
            try(ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(LocationRowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new ResourceNotFoundException("Exception while finding location by id.");
        }
    }

    @Override
    public Optional<Location> findByName(String locationName) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_NAME,
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            statement.setString(1, locationName);
            try(ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(LocationRowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new ResourceNotFoundException("Exception while finding location by name.");
        }
    }

    @Override
    public List<Location> findAllLocationsByUserId(Long userId) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(FIND_ALL_LOCATIONS_BY_USER_ID,
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {

            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                return LocationRowMapper.mapRows(rs);
            }

        } catch (SQLException e) {
            throw new ResourceNotFoundException("Exception while finding locations by user id.");
        }
    }

    @Override
    public Optional<Location> findByUserIdAndLocationName(Long userId, String nameLocation) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_USER_ID_AND_LOCATION_NAME)) {

            statement.setLong(1, userId);
            statement.setString(2, nameLocation);
            try(ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(LocationRowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new ResourceNotFoundException("Exception while finding locations by user id and location name.");
        }
    }

    @Override
    public void save(User user, Location location) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(SAVE, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, location.getName());
            statement.setDouble(2, location.getLatitude());
            statement.setDouble(3, location.getLongitude());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()){
                rs.next();
                location.setId(rs.getLong(1));
            }

            saveUsersLocations(user.getId(),location.getId());

        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while creating location.");
        }
    }

    @Override
    public void saveUsersLocations(Long userId, Long locationId) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(SAVE_USERS_LOCATIONS)) {

            statement.setLong(1, userId);
            statement.setLong(2, locationId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while save user location.");
        }

    }

    @Override
    public void delete(Long id) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(DELETE)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while deleting location.");
        }
    }

    @Override
    public void deleteUsersLocationsWhereUserIdAndLocationId(Long userId, Long locationId) {
        try (Connection connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(DELETE_USERS_LOCATIONS_WHERE_USER_ID_AND_LOCATION_ID)) {

            statement.setLong(1, userId);
            statement.setLong(2, locationId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while deleting location by user id.");
        }
    }
}
