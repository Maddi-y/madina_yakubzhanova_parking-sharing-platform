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

    private Favourite mapRow(ResultSet rs)
            throws SQLException {

        User user = new User();

        user.setUserId(
                rs.getLong("user_id")
        );

        ParkingSpot parkingSpot =
                new ParkingSpot();

        parkingSpot.setSpotId(
                rs.getLong("spot_id")
        );

        return new Favourite(
                rs.getLong("favourite_id"),
                user,
                parkingSpot,
                rs.getTimestamp("created_at")
                        .toLocalDateTime()
        );
    }

    @Override
    public Favourite save(Favourite entity) {

        if (entity.getUser() == null
                || entity.getUser().getUserId() == null) {

            throw new DaoException(
                    "Favourite user must not be null"
            );
        }

        if (entity.getParkingSpot() == null
                || entity.getParkingSpot().getSpotId() == null) {

            throw new DaoException(
                    "Favourite parking spot must not be null"
            );
        }

        Connection connection = null;

        try {

            connection = getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 INSERT_FAVOURITE,
                                 Statement.RETURN_GENERATED_KEYS
                         )) {

                statement.setLong(
                        1,
                        entity.getUser().getUserId()
                );

                statement.setLong(
                        2,
                        entity.getParkingSpot().getSpotId()
                );

                int affectedRows =
                        statement.executeUpdate();

                if (affectedRows == 0) {

                    throw new DaoException(
                            "Failed to save favourite"
                    );
                }

                try (ResultSet generatedKeys =
                             statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {

                        entity.setFavouriteId(
                                generatedKeys.getLong(1)
                        );

                    } else {

                        throw new DaoException(
                                "Failed to obtain generated favourite id"
                        );
                    }
                }

                LOGGER.info(
                        "Favourite saved successfully. ID={}",
                        entity.getFavouriteId()
                );

                return entity;
            }

        } catch (SQLException e) {

            LOGGER.error(
                    "Error saving favourite",
                    e
            );

            throw new DaoException(
                    "Failed to save favourite",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    @Override
    public Optional<Favourite> findById(Long id) {

        Connection connection = null;

        try {

            connection = getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 FIND_BY_ID
                         )) {

                statement.setLong(1, id);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {

                        return Optional.of(
                                mapRow(resultSet)
                        );
                    }

                    return Optional.empty();
                }
            }

        } catch (SQLException e) {

            LOGGER.error(
                    "Error finding favourite by id={}",
                    id,
                    e
            );

            throw new DaoException(
                    "Failed to find favourite by id",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    @Override
    public List<Favourite> findAll(
            int page,
            int size
    ) {

        List<Favourite> favourites =
                new ArrayList<>();

        int offset = (page - 1) * size;

        Connection connection = null;

        try {

            connection = getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 FIND_ALL
                         )) {

                statement.setInt(1, size);
                statement.setInt(2, offset);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    while (resultSet.next()) {

                        favourites.add(
                                mapRow(resultSet)
                        );
                    }
                }

                return favourites;
            }

        } catch (SQLException e) {

            LOGGER.error(
                    "Error finding all favourites",
                    e
            );

            throw new DaoException(
                    "Failed to find all favourites",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    @Override
    public Favourite update(
            Favourite entity
    ) {

        Connection connection = null;

        try {

            connection = getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 UPDATE_FAVOURITE
                         )) {

                statement.setLong(
                        1,
                        entity.getUser().getUserId()
                );

                statement.setLong(
                        2,
                        entity.getParkingSpot().getSpotId()
                );

                statement.setLong(
                        3,
                        entity.getFavouriteId()
                );

                int affectedRows =
                        statement.executeUpdate();

                if (affectedRows == 0) {

                    throw new DaoException(
                            "Favourite not found. ID="
                                    + entity.getFavouriteId()
                    );
                }

                LOGGER.info(
                        "Favourite updated successfully. ID={}",
                        entity.getFavouriteId()
                );

                return entity;
            }

        } catch (SQLException e) {

            LOGGER.error(
                    "Error updating favourite. ID={}",
                    entity.getFavouriteId(),
                    e
            );

            throw new DaoException(
                    "Failed to update favourite",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    @Override
    public boolean deleteById(
            Long id
    ) {

        Connection connection = null;

        try {

            connection = getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 DELETE_BY_ID
                         )) {

                statement.setLong(
                        1,
                        id
                );

                int affectedRows =
                        statement.executeUpdate();

                boolean deleted =
                        affectedRows > 0;

                if (deleted) {

                    LOGGER.info(
                            "Favourite deleted successfully. ID={}",
                            id
                    );
                }

                return deleted;
            }

        } catch (SQLException e) {

            LOGGER.error(
                    "Error deleting favourite. ID={}",
                    id,
                    e
            );

            throw new DaoException(
                    "Failed to delete favourite",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    @Override
    public List<Favourite> findByUserId(
            Long userId,
            int page,
            int size
    ) {

        List<Favourite> favourites =
                new ArrayList<>();

        int offset = (page - 1) * size;

        Connection connection = null;

        try {

            connection = getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 FIND_BY_USER_ID
                         )) {

                statement.setLong(1, userId);
                statement.setInt(2, size);
                statement.setInt(3, offset);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    while (resultSet.next()) {

                        favourites.add(
                                mapRow(resultSet)
                        );
                    }
                }

                return favourites;
            }

        } catch (SQLException e) {

            LOGGER.error(
                    "Error finding favourites by userId={}",
                    userId,
                    e
            );

            throw new DaoException(
                    "Failed to find favourites by user id",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    @Override
    public boolean exists(
            Long userId,
            Long spotId
    ) {

        Connection connection = null;

        try {

            connection = getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 EXISTS_FAVOURITE
                         )) {

                statement.setLong(
                        1,
                        userId
                );

                statement.setLong(
                        2,
                        spotId
                );

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {

                        return resultSet.getBoolean(1);
                    }

                    return false;
                }
            }

        } catch (SQLException e) {

            LOGGER.error(
                    "Error checking favourite. userId={}, spotId={}",
                    userId,
                    spotId,
                    e
            );

            throw new DaoException(
                    "Failed to check favourite existence",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }
}
