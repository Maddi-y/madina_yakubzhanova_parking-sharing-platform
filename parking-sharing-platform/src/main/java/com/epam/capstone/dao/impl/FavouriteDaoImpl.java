package com.epam.capstone.dao.impl;

import com.epam.capstone.dao.FavouriteDao;
import com.epam.capstone.exception.DaoException;
import com.epam.capstone.model.Favourite;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.pool.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FavouriteDaoImpl extends BaseDao implements FavouriteDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(FavouriteDaoImpl.class);

    private static final String INSERT_FAVOURITE = """
            INSERT INTO favourites (
                user_id,
                spot_id
            )
            VALUES (?, ?)
            """;

    private static final String FIND_BY_ID = """
            SELECT *
            FROM favourites
            WHERE favourite_id = ?
            """;

    private static final String FIND_ALL = """
            SELECT *
            FROM favourites
            ORDER BY favourite_id
            LIMIT ?
            OFFSET ?
            """;

    private static final String UPDATE_FAVOURITE = """
            UPDATE favourites
            SET user_id = ?,
                spot_id = ?
            WHERE favourite_id = ?
            """;

    private static final String DELETE_BY_ID = """
            DELETE
            FROM favourites
            WHERE favourite_id = ?
            """;

    private static final String FIND_BY_USER_ID = """
            SELECT *
            FROM favourites
            WHERE user_id = ?
            ORDER BY favourite_id
            LIMIT ?
            OFFSET ?
            """;

    private static final String EXISTS_FAVOURITE = """
            SELECT EXISTS (
                SELECT 1
                FROM favourites
                WHERE user_id = ?
                AND spot_id = ?
            )
            """;

    public FavouriteDaoImpl(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private Favourite mapRow(ResultSet rs) throws SQLException {

        User user = new User();
        user.setUserId(rs.getLong("user_id"));

        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setSpotId(rs.getLong("spot_id"));

        return new Favourite(
                rs.getLong("favourite_id"),
                user,
                parkingSpot,
                rs.getTimestamp("created_at").toLocalDateTime());
    }

    @Override
    public Favourite save(Favourite entity) {

        if (entity.getUser() == null || entity.getUser().getUserId() == null) {
            throw new DaoException("Favourite user must not be null");
        }

        if (entity.getParkingSpot() == null || entity.getParkingSpot().getSpotId() == null) {
            throw new DaoException("Favourite parking spot must not be null");
        }

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(
                    INSERT_FAVOURITE, Statement.RETURN_GENERATED_KEYS)) {

                statement.setLong(1, entity.getUser().getUserId());
                statement.setLong(2, entity.getParkingSpot().getSpotId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Failed to save favourite");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {
                        entity.setFavouriteId(generatedKeys.getLong(1));
                    } else {
                        throw new DaoException("Failed to obtain generated favourite id");
                    }
                }

                LOGGER.info("Favourite saved successfully. ID={}", entity.getFavouriteId());
                return entity;
            }
        }, "Error saving favourite");
    }

    @Override
    public Optional<Favourite> findById(Long id) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_BY_ID)) {

                statement.setLong(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {
                        return Optional.of(mapRow(resultSet));
                    }
                    return Optional.empty();
                }
            }
        }, "Error finding favourite by id");
    }

    @Override
    public List<Favourite> findAll(int page, int size) {

        return execute(connection -> {

            List<Favourite> favourites = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_ALL)) {

                statement.setInt(1, size);
                statement.setInt(2, calculateOffset(page, size));

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        favourites.add(mapRow(resultSet));
                    }
                }
                return favourites;
            }
        }, "Error finding all favourites");
    }

    @Override
    public Favourite update(Favourite entity) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(UPDATE_FAVOURITE)) {

                statement.setLong(1, entity.getUser().getUserId());
                statement.setLong(2, entity.getParkingSpot().getSpotId());
                statement.setLong(3, entity.getFavouriteId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Favourite not found. ID=" + entity.getFavouriteId());
                }

                LOGGER.info("Favourite updated successfully. ID={}", entity.getFavouriteId()
                );
                return entity;
            }
        }, "Error updating favourite");
    }

    @Override
    public boolean deleteById(Long id) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(DELETE_BY_ID)) {

                statement.setLong(1, id);

                int affectedRows = statement.executeUpdate();

                boolean deleted = affectedRows > 0;

                if (deleted) {
                    LOGGER.info("Favourite deleted successfully. ID={}", id);
                }
                return deleted;
            }
        }, "Error deleting favourite");
    }

    @Override
    public List<Favourite> findByUserId(Long userId, int page, int size) {

        return execute(connection -> {

            List<Favourite> favourites = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_BY_USER_ID)) {

                statement.setLong(1, userId);
                statement.setInt(2, size);
                statement.setInt(3, calculateOffset(page, size));

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        favourites.add(mapRow(resultSet));
                    }
                }
                return favourites;
            }
        }, "Error finding favourites by userId");
    }

    @Override
    public boolean exists(Long userId, Long spotId) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(EXISTS_FAVOURITE)) {

                statement.setLong(1, userId);
                statement.setLong(2, spotId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {
                        return resultSet.getBoolean(1);
                    }
                    return false;
                }
            }

        }, "Error checking favourite existence");
    }
}
