package com.epam.capstone.dao.impl;

import com.epam.capstone.dao.ParkingSpotDao;
import com.epam.capstone.dao.sql.ParkingSpotSql;
import com.epam.capstone.exception.DaoException;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.enums.ParkingSpotStatus;
import com.epam.capstone.pool.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ParkingSpotDaoImpl extends BaseDao implements ParkingSpotDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingSpotDaoImpl.class);

    public ParkingSpotDaoImpl(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private ParkingSpot mapRow(ResultSet resultSet) throws SQLException {

        return new ParkingSpot(
                resultSet.getLong("spot_id"),
                resultSet.getLong("parking_id"),
                resultSet.getInt("spot_number"),
                ParkingSpotStatus.valueOf(resultSet.getString("status")),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Override
    public ParkingSpot save(ParkingSpot entity) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(
                                 ParkingSpotSql.INSERT,
                                 Statement.RETURN_GENERATED_KEYS)) {

                statement.setLong(1, entity.getParkingId());
                statement.setInt(2, entity.getSpotNumber());
                statement.setString(3, entity.getStatus().name());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Failed to save parking spot");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {

                        entity.setSpotId(generatedKeys.getLong(1));

                        LOGGER.info("Parking spot saved successfully. ID={}", entity.getSpotId());

                        return entity;
                    }
                }

                throw new DaoException("Failed to retrieve generated parking spot id");
            }

        }, "Error saving parking spot");
    }

    @Override
    public Optional<ParkingSpot> findById(Long spotId) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(
                                 ParkingSpotSql.FIND_BY_ID)) {

                statement.setLong(1, spotId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        ParkingSpot parkingSpot = mapRow(resultSet);

                        LOGGER.info("Parking spot found. ID={}", spotId);

                        return Optional.of(parkingSpot);
                    }

                    LOGGER.info("Parking spot not found. ID={}", spotId);

                    return Optional.empty();
                }
            }

        }, "Failed to find parking spot by id=" + spotId);
    }

    @Override
    public List<ParkingSpot> findAll(int page, int size) {

        return execute(connection -> {

            List<ParkingSpot> parkingSpots = new ArrayList<>();

            int offset = calculateOffset(page, size);

            try (PreparedStatement statement =
                         connection.prepareStatement(ParkingSpotSql.FIND_ALL)) {

                statement.setInt(1, size);
                statement.setInt(2, offset);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        parkingSpots.add(mapRow(resultSet));
                    }
                }
            }

            LOGGER.info(
                    "Retrieved {} parking spots. Page={}, Size={}",
                    parkingSpots.size(),
                    page,
                    size);

            return parkingSpots;

        }, "Failed to retrieve parking spots");
    }

    @Override
    public ParkingSpot update(ParkingSpot entity) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(ParkingSpotSql.UPDATE)) {

                statement.setLong(1, entity.getParkingId());
                statement.setInt(2, entity.getSpotNumber());
                statement.setString(3, entity.getStatus().name());
                statement.setLong(4, entity.getSpotId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException(
                            "Parking spot not found. ID=" + entity.getSpotId());
                }

                LOGGER.info(
                        "Parking spot updated successfully. ID={}",
                        entity.getSpotId());

                return entity;
            }

        }, "Error updating parking spot");
    }

    @Override
    public boolean deleteById(Long spotId) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(ParkingSpotSql.DELETE)) {

                statement.setLong(1, spotId);

                int affectedRows = statement.executeUpdate();

                boolean deleted = affectedRows > 0;

                if (deleted) {

                    LOGGER.info(
                            "Parking spot deleted successfully. ID={}",
                            spotId);

                } else {

                    LOGGER.info(
                            "Parking spot not found for deletion. ID={}",
                            spotId);
                }

                return deleted;
            }

        }, "Failed to delete parking spot. ID=" + spotId);
    }

    @Override
    public List<ParkingSpot> findByParkingId(Long parkingId) {

        return execute(connection -> {

            List<ParkingSpot> parkingSpots = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(ParkingSpotSql.FIND_BY_PARKING)) {

                statement.setLong(1, parkingId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        parkingSpots.add(mapRow(resultSet));
                    }
                }
            }

            LOGGER.info(
                    "Found {} parking spots for parking ID={}",
                    parkingSpots.size(),
                    parkingId);

            return parkingSpots;

        }, "Failed to find parking spots by parking id=" + parkingId);
    }

    @Override
    public List<ParkingSpot> findAvailableByParkingId(Long parkingId) {

        return execute(connection -> {

            List<ParkingSpot> parkingSpots = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 ParkingSpotSql.FIND_AVAILABLE_BY_PARKING)) {

                statement.setLong(1, parkingId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        parkingSpots.add(mapRow(resultSet));
                    }
                }
            }

            LOGGER.info(
                    "Found {} available parking spots for parking ID={}",
                    parkingSpots.size(),
                    parkingId);

            return parkingSpots;

        }, "Failed to find available parking spots for parking id=" + parkingId);
    }

    @Override
    public boolean existsByParkingAndSpotNumber(Long parkingId,
                                                Integer spotNumber) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 ParkingSpotSql.EXISTS_BY_PARKING_AND_NUMBER)) {

                statement.setLong(1, parkingId);
                statement.setInt(2, spotNumber);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        boolean exists = resultSet.getBoolean(1);

                        LOGGER.info(
                                "Parking spot existence check. Parking ID={}, Spot number={}, Exists={}",
                                parkingId,
                                spotNumber,
                                exists);

                        return exists;
                    }

                    return false;
                }
            }

        }, "Failed to check parking spot existence");
    }

    @Override
    public boolean existsById(Long spotId) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 ParkingSpotSql.EXISTS_BY_ID)) {

                statement.setLong(1, spotId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        boolean exists = resultSet.getBoolean(1);

                        LOGGER.info(
                                "Parking spot existence check. ID={}, Exists={}",
                                spotId,
                                exists);

                        return exists;
                    }

                    return false;
                }
            }

        }, "Failed to check parking spot existence by id=" + spotId);
    }

    @Override
    public void updateStatus(Long spotId,
                             ParkingSpotStatus status) {

        execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 ParkingSpotSql.UPDATE_STATUS)) {

                statement.setString(1, status.name());
                statement.setLong(2, spotId);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException(
                            "Parking spot not found. ID=" + spotId);
                }

                LOGGER.info(
                        "Parking spot status updated successfully. ID={}, Status={}",
                        spotId,
                        status);

                return null;
            }

        }, "Failed to update parking spot status");
    }
}