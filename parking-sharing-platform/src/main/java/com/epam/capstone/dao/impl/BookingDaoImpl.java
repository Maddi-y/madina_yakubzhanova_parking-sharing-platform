package com.epam.capstone.dao.impl;

import com.epam.capstone.dao.BookingDao;
import com.epam.capstone.exception.DaoException;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.BookingStatus;
import com.epam.capstone.pool.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDaoImpl extends BaseDao implements BookingDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingDaoImpl.class);

    private static final String INSERT_BOOKING = """
            INSERT INTO bookings (
                driver_id,
                spot_id,
                start_time,
                end_time,
                total_price,
                status
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String FIND_BY_ID = """
            SELECT *
            FROM bookings
            WHERE booking_id = ?
            """;

    private static final String FIND_ALL = """
            SELECT *
            FROM bookings
            ORDER BY booking_id
            LIMIT ?
            OFFSET ?
            """;

    private static final String UPDATE_BOOKING = """
            UPDATE bookings
            SET driver_id = ?,
                spot_id = ?,
                start_time = ?,
                end_time = ?,
                total_price = ?,
                status = ?
            WHERE booking_id = ?
            """;

    private static final String DELETE_BY_ID = """
            DELETE
            FROM bookings
            WHERE booking_id = ?
            """;

    private static final String FIND_BY_DRIVER_ID = """
            SELECT *
            FROM bookings
            WHERE driver_id = ?
            ORDER BY booking_id
            LIMIT ?
            OFFSET ?
            """;

    private static final String FIND_BY_SPOT_ID = """
            SELECT *
            FROM bookings
            WHERE spot_id = ?
            ORDER BY booking_id
            LIMIT ?
            OFFSET ?
            """;

    private static final String FIND_BY_SPOT_ID_ALL = """
            SELECT *
            FROM bookings
            WHERE spot_id = ?
            """;

    private static final String FIND_BY_STATUS = """
            SELECT *
            FROM bookings
            WHERE status = ?
            ORDER BY booking_id
            LIMIT ?
            OFFSET ?
            """;

    public BookingDaoImpl(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private Booking mapRow(ResultSet resultSet) throws SQLException {

        User driver = new User();
        driver.setUserId(resultSet.getLong("driver_id"));

        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setSpotId(resultSet.getLong("spot_id"));

        return new Booking(
                resultSet.getLong("booking_id"),
                driver,
                parkingSpot,
                resultSet.getTimestamp("start_time").toLocalDateTime(),
                resultSet.getTimestamp("end_time").toLocalDateTime(),
                resultSet.getBigDecimal("total_price"),
                BookingStatus.valueOf(resultSet.getString("status")),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Override
    public Booking save(Booking entity) {

        if (entity.getDriver() == null || entity.getDriver().getUserId() == null) {
            throw new DaoException("Booking driver must not be null");
        }

        if (entity.getParkingSpot() == null || entity.getParkingSpot().getSpotId() == null) {
            throw new DaoException("Booking parking spot must not be null");
        }

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(INSERT_BOOKING, Statement.RETURN_GENERATED_KEYS)) {

                statement.setLong(1, entity.getDriver().getUserId());
                statement.setLong(2, entity.getParkingSpot().getSpotId());
                statement.setTimestamp(3, Timestamp.valueOf(entity.getStartTime()));
                statement.setTimestamp(4, Timestamp.valueOf(entity.getEndTime()));
                statement.setBigDecimal(5, entity.getTotalPrice());
                statement.setString(6, entity.getStatus().name());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Failed to save booking");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setBookingId(generatedKeys.getLong(1));
                    } else {
                        throw new DaoException("Failed to obtain generated booking id");
                    }
                }

                LOGGER.info("Booking saved successfully. ID={}", entity.getBookingId());
                return entity;

            } catch (SQLException e) {
                throw new DaoException("Failed to save booking", e);
            }
        }, "Error saving booking");
    }


    @Override
    public Optional<Booking> findById(Long id) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {

                statement.setLong(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(mapRow(resultSet));
                    }

                    return Optional.empty();
                }

            } catch (SQLException e) {
                throw new DaoException("Failed to find booking by id", e);
            }

        }, "Error finding booking by id");
    }

    @Override
    public List<Booking> findAll(int page, int size) {

        return execute(connection -> {

            List<Booking> bookings = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_ALL)) {

                statement.setInt(1, size);
                statement.setInt(2, calculateOffset(page, size));

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        bookings.add(mapRow(resultSet));
                    }
                }

                return bookings;
            } catch (SQLException e) {
                throw new DaoException("Failed to find all bookings", e);
            }

        }, "Error finding all bookings");
    }

    @Override
    public Booking update(Booking entity) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_BOOKING)) {

                statement.setLong(1, entity.getDriver().getUserId());
                statement.setLong(2, entity.getParkingSpot().getSpotId());
                statement.setTimestamp(3, Timestamp.valueOf(entity.getStartTime()));
                statement.setTimestamp(4, Timestamp.valueOf(entity.getEndTime()));
                statement.setBigDecimal(5, entity.getTotalPrice());
                statement.setString(6, entity.getStatus().name());
                statement.setLong(7, entity.getBookingId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Booking not found. ID=" + entity.getBookingId());
                }

                LOGGER.info("Booking updated successfully. ID={}", entity.getBookingId());
                return entity;

            } catch (SQLException e) {
                throw new DaoException("Failed to update booking", e);
            }

        }, "Error updating booking");
    }

    @Override
    public boolean deleteById(Long id) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {

                statement.setLong(1, id);

                int affectedRows = statement.executeUpdate();

                boolean deleted = affectedRows > 0;

                if (deleted) {
                    LOGGER.info("Booking deleted successfully. ID={}", id);
                }

                return deleted;

            } catch (SQLException e) {
                throw new DaoException("Failed to delete booking", e);
            }

        }, "Error deleting booking");
    }

    @Override
    public List<Booking> findByDriverId(Long driverId, int page, int size) {

        return execute(connection -> {

            List<Booking> bookings = new ArrayList<>();

            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_DRIVER_ID)) {

                statement.setLong(1, driverId);
                statement.setInt(2, size);
                statement.setInt(3, calculateOffset(page, size));

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        bookings.add(mapRow(resultSet)
                        );
                    }
                }
                return bookings;

            } catch (SQLException e) {
                throw new DaoException("Failed to find bookings by driver id", e);
            }

        }, "Error finding bookings by driverId=" + driverId);
    }

    @Override
    public List<Booking> findByParkingSpotId(Long spotId, int page, int size) {

        return execute(connection -> {

            List<Booking> bookings = new ArrayList<>();

            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_SPOT_ID)) {

                statement.setLong(1, spotId);
                statement.setInt(2, size);
                statement.setInt(3, calculateOffset(page, size));

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        bookings.add(mapRow(resultSet));
                    }
                }
                return bookings;

            } catch (SQLException e) {
                throw new DaoException("Failed to find bookings by parking spot id", e);
            }

        }, "Error finding bookings by spotId=" + spotId);
    }

    @Override
    public List<Booking> findByParkingSpotId(Long spotId) {

        return execute(connection -> {

            List<Booking> bookings = new ArrayList<>();

            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_SPOT_ID_ALL)) {
                statement.setLong(1, spotId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        bookings.add(mapRow(resultSet));
                    }
                }

                return bookings;

            } catch (SQLException e) {
                throw new DaoException(
                        "Failed to find bookings by parking spot id", e);
            }
        }, "Error finding bookings by spotId=" + spotId);
    }

    @Override
    public List<Booking> findByStatus(BookingStatus status, int page, int size) {

        return execute(connection -> {

            List<Booking> bookings = new ArrayList<>();

            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_STATUS)) {

                statement.setString(1, status.name());
                statement.setInt(2, size);
                statement.setInt(3, calculateOffset(page, size));

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        bookings.add(mapRow(resultSet));
                    }
                }
                return bookings;

            } catch (SQLException e) {
                throw new DaoException("Failed to find bookings by status", e);
            }

        }, "Error finding bookings by status=" + status);
    }
}
