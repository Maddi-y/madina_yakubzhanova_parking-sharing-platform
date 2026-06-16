package com.epam.capstone.dao.impl;

import com.epam.capstone.dao.ParkingSpotDao;
import com.epam.capstone.exception.DaoException;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.ParkingSpotStatus;
import com.epam.capstone.pool.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParkingSpotDaoImpl extends BaseDao implements ParkingSpotDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingSpotDaoImpl.class);

    private static final String INSERT_SPOT = """
            INSERT INTO parking_spots (
                owner_id,
                title,
                address,
                description,
                hourly_rate,
                status,
                latitude,
                longitude
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String FIND_BY_ID = """
            SELECT *
            FROM parking_spots
            WHERE spot_id = ?
            """;

    private static final String FIND_ALL = """
            SELECT *
            FROM parking_spots
            ORDER BY spot_id
            LIMIT ?
            OFFSET ?
            """;

    private static final String UPDATE_SPOT = """
            UPDATE parking_spots
            SET owner_id = ?,
                title = ?,
                address = ?,
                description = ?,
                hourly_rate = ?,
                status = ?,
                latitude = ?,
                longitude = ?
            WHERE spot_id = ?
            """;

    private static final String DELETE_BY_ID = """
            DELETE
            FROM parking_spots
            WHERE spot_id = ?
            """;

    private static final String FIND_BY_OWNER_ID = """
            SELECT *
            FROM parking_spots
            WHERE owner_id = ?
            ORDER BY spot_id
            LIMIT ?
            OFFSET ?
            """;

    private static final String FIND_AVAILABLE = """
            SELECT *
            FROM parking_spots
            WHERE status = 'AVAILABLE'
            ORDER BY spot_id
            LIMIT ?
            OFFSET ?
            """;

    public ParkingSpotDaoImpl(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private ParkingSpot mapRow(ResultSet resultSet) throws SQLException {

        User owner = new User();
        owner.setUserId(resultSet.getLong("owner_id"));

        return new ParkingSpot(
                resultSet.getLong("spot_id"),
                owner,
                resultSet.getString("title"),
                resultSet.getString("address"),
                resultSet.getString("description"),
                resultSet.getBigDecimal("hourly_rate"),
                ParkingSpotStatus.valueOf(resultSet.getString("status")),
                resultSet.getBigDecimal("latitude"),
                resultSet.getBigDecimal("longitude"),
                resultSet.getTimestamp("created_at")
                        .toLocalDateTime()
        );
    }

    @Override
    public ParkingSpot save(ParkingSpot entity) {

        if (entity.getOwner() == null || entity.getOwner().getUserId() == null) {
            throw new DaoException("Parking spot owner must not be null");
        }

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(INSERT_SPOT, Statement.RETURN_GENERATED_KEYS)) {

                statement.setLong(1, entity.getOwner().getUserId());
                statement.setString(2, entity.getTitle());
                statement.setString(3, entity.getAddress());
                statement.setString(4, entity.getDescription());
                statement.setBigDecimal(5, entity.getHourlyRate());
                statement.setString(6, entity.getStatus().name());
                statement.setBigDecimal(7, entity.getLatitude());
                statement.setBigDecimal(8, entity.getLongitude());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Failed to save parking spot");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {
                        entity.setSpotId(generatedKeys.getLong(1));
                    } else {
                        throw new DaoException("Failed to obtain generated parking spot id");
                    }
                }

                LOGGER.info("Parking spot saved successfully. ID={}", entity.getSpotId());
                return entity;
            }

        }, "Error saving parking spot");
    }


    @Override
    public Optional<ParkingSpot> findById(Long id) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {

                statement.setLong(1, id);

                try (ResultSet rs = statement.executeQuery()) {

                    if (rs.next()) {
                        return Optional.of(mapRow(rs));
                    }
                    return Optional.empty();
                }
            }
        }, "Error finding parking spot by id");
    }

    @Override
    public List<ParkingSpot> findAll(int page, int size) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {

                statement.setInt(1, size);
                statement.setInt(2, calculateOffset(page, size));

                List<ParkingSpot> parkingSpots = new ArrayList<>();

                try (ResultSet rs = statement.executeQuery()) {

                    while (rs.next()) {
                        parkingSpots.add(mapRow(rs));
                    }
                }
                return parkingSpots;
            }
        }, "Error finding all parking spots");
    }

    @Override
    public ParkingSpot update(ParkingSpot entity) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(UPDATE_SPOT)) {

                statement.setLong(1, entity.getOwner().getUserId());
                statement.setString(2, entity.getTitle());
                statement.setString(3, entity.getAddress());
                statement.setString(4, entity.getDescription());
                statement.setBigDecimal(5, entity.getHourlyRate());
                statement.setString(6, entity.getStatus().name());
                statement.setBigDecimal(7, entity.getLatitude());
                statement.setBigDecimal(8, entity.getLongitude());
                statement.setLong(9, entity.getSpotId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Parking spot not found. ID=" + entity.getSpotId());
                }

                LOGGER.info("Parking spot updated successfully. ID={}", entity.getSpotId());
                return entity;
            }
        }, "Error updating parking spot");
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
                    LOGGER.info("Parking spot deleted successfully. ID={}", id);
                } else {
                    LOGGER.warn("Parking spot not found. ID={}", id);
                }
                return deleted;
            }
        }, "Error deleting parking spot");
    }

    @Override
    public List<ParkingSpot> findByOwnerId(Long ownerId, int page, int size) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_BY_OWNER_ID)) {

                statement.setLong(1, ownerId);
                statement.setInt(2, size);
                statement.setInt(3, calculateOffset(page, size));

                List<ParkingSpot> parkingSpots = new ArrayList<>();

                try (ResultSet rs = statement.executeQuery()) {

                    while (rs.next()) {
                        parkingSpots.add(mapRow(rs));
                    }
                }

                LOGGER.info("Found {} parking spots for owner ID={}", parkingSpots.size(), ownerId);
                return parkingSpots;
            }
        }, "Error finding parking spots by owner");
    }

    @Override
    public List<ParkingSpot> findAvailable(int page, int size) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_AVAILABLE)) {

                statement.setInt(1, size);
                statement.setInt(2, calculateOffset(page, size));

                List<ParkingSpot> parkingSpots = new ArrayList<>();

                try (ResultSet rs = statement.executeQuery()) {

                    while (rs.next()) {
                        parkingSpots.add(mapRow(rs));
                    }
                }

                LOGGER.info("Found {} available parking spots", parkingSpots.size());
                return parkingSpots;
            }
        }, "Error finding available parking spots");
    }
}
