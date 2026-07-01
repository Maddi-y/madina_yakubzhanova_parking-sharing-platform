package com.epam.capstone.dao.impl;

import com.epam.capstone.dao.ParkingDao;
import com.epam.capstone.dao.sql.ParkingSql;
import com.epam.capstone.exception.DaoException;
import com.epam.capstone.model.Parking;
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

@Repository
public class ParkingDaoImpl extends BaseDao implements ParkingDao {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ParkingDaoImpl.class);

    public ParkingDaoImpl(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private Parking mapRow(ResultSet resultSet) throws SQLException {

        return new Parking(
                resultSet.getLong("parking_id"),
                resultSet.getLong("owner_id"),
                resultSet.getLong("address_id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getBigDecimal("hourly_rate"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Override
    public Parking create(Parking parking) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 ParkingSql.INSERT,
                                 Statement.RETURN_GENERATED_KEYS)) {

                statement.setLong(1, parking.getOwnerId());
                statement.setLong(2, parking.getAddressId());
                statement.setString(3, parking.getTitle());
                statement.setString(4, parking.getDescription());
                statement.setBigDecimal(5, parking.getHourlyRate());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Failed to save parking");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {

                        parking.setParkingId(generatedKeys.getLong(1));

                        LOGGER.info(
                                "Parking saved successfully. ID={}",
                                parking.getParkingId());

                        return parking;
                    }
                }

                throw new DaoException("Failed to retrieve generated parking id");
            }

        }, "Error saving parking");
    }

    @Override
    public Parking findById(Long parkingId) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(ParkingSql.FIND_BY_ID)) {

                statement.setLong(1, parkingId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        Parking parking = mapRow(resultSet);

                        LOGGER.info(
                                "Parking found by id={}",
                                parkingId);

                        return parking;
                    }

                    throw new DaoException(
                            "Parking not found with id=" + parkingId);
                }
            }

        }, "Failed to find parking by id=" + parkingId);
    }

    @Override
    public List<Parking> findAll() {

        return execute(connection -> {

            List<Parking> parkings = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(ParkingSql.FIND_ALL);

                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    parkings.add(mapRow(resultSet));
                }
            }

            LOGGER.info(
                    "Retrieved {} parkings",
                    parkings.size());

            return parkings;

        }, "Failed to retrieve parkings");
    }

    @Override
    public Parking update(Parking parking) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(ParkingSql.UPDATE)) {

                statement.setLong(1, parking.getOwnerId());
                statement.setLong(2, parking.getAddressId());
                statement.setString(3, parking.getTitle());
                statement.setString(4, parking.getDescription());
                statement.setBigDecimal(5, parking.getHourlyRate());
                statement.setLong(6, parking.getParkingId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException(
                            "Parking not found with id=" + parking.getParkingId());
                }

                LOGGER.info(
                        "Parking updated successfully. ID={}",
                        parking.getParkingId());

                return parking;
            }

        }, "Error updating parking with id=" + parking.getParkingId());
    }

    @Override
    public void delete(Long parkingId) {

        execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(ParkingSql.DELETE)) {

                statement.setLong(1, parkingId);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException(
                            "Parking not found with id=" + parkingId);
                }

                LOGGER.info(
                        "Parking deleted successfully. ID={}",
                        parkingId);

                return null;
            }

        }, "Failed to delete parking with id=" + parkingId);
    }

    @Override
    public List<Parking> findByOwnerId(Long ownerId) {

        return execute(connection -> {

            List<Parking> parkings = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(ParkingSql.FIND_BY_OWNER_ID)) {

                statement.setLong(1, ownerId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        parkings.add(mapRow(resultSet));
                    }
                }
            }

            LOGGER.info(
                    "Found {} parkings for owner={}",
                    parkings.size(),
                    ownerId);

            return parkings;

        }, "Failed to find parkings by owner id=" + ownerId);
    }

    @Override
    public List<Parking> findByCountryAndCity(String country, String city) {

        return execute(connection -> {

            List<Parking> parkings = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(ParkingSql.FIND_BY_COUNTRY_AND_CITY)) {

                statement.setString(1, country);
                statement.setString(2, city);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        parkings.add(mapRow(resultSet));
                    }
                }
            }

            LOGGER.info(
                    "Found {} parkings in {}, {}",
                    parkings.size(),
                    country,
                    city);

            return parkings;

        }, "Failed to find parkings by country and city");
    }

    @Override
    public List<Parking> findByStreet(String country,
                                      String city,
                                      String street) {

        return execute(connection -> {

            List<Parking> parkings = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(ParkingSql.FIND_BY_STREET)) {

                statement.setString(1, country);
                statement.setString(2, city);
                statement.setString(3, street);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        parkings.add(mapRow(resultSet));
                    }
                }
            }

            LOGGER.info(
                    "Found {} parkings on street={} ({}, {})",
                    parkings.size(),
                    street,
                    country,
                    city);

            return parkings;

        }, "Failed to find parkings by street");
    }
}