package com.epam.capstone.dao.impl;

import com.epam.capstone.dao.AddressDao;
import com.epam.capstone.dao.sql.AddressSql;
import com.epam.capstone.exception.DaoException;
import com.epam.capstone.model.Address;
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
public class AddressDaoImpl extends BaseDao implements AddressDao {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AddressDaoImpl.class);

    public AddressDaoImpl(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private Address mapRow(ResultSet resultSet) throws SQLException {

        return new Address(
                resultSet.getLong("address_id"),
                resultSet.getString("country"),
                resultSet.getString("city"),
                resultSet.getString("street"),
                resultSet.getString("house_number"),
                resultSet.getBigDecimal("latitude"),
                resultSet.getBigDecimal("longitude")
        );
    }

    @Override
    public Address create(Address address) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 AddressSql.INSERT,
                                 Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, address.getCountry());
                statement.setString(2, address.getCity());
                statement.setString(3, address.getStreet());
                statement.setString(4, address.getHouseNumber());
                statement.setBigDecimal(5, address.getLatitude());
                statement.setBigDecimal(6, address.getLongitude());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Failed to save address");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {

                        address.setAddressId(generatedKeys.getLong(1));

                        LOGGER.info(
                                "Address saved successfully. ID={}",
                                address.getAddressId());

                        return address;
                    }
                }

                throw new DaoException("Failed to retrieve generated address id");
            }

        }, "Error saving address");
    }

    @Override
    public Address findById(Long addressId) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(AddressSql.FIND_BY_ID)) {

                statement.setLong(1, addressId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        Address address = mapRow(resultSet);

                        LOGGER.info(
                                "Address found by id={}",
                                addressId);

                        return address;
                    }

                    throw new DaoException(
                            "Address not found with id=" + addressId);
                }
            }

        }, "Failed to find address by id=" + addressId);
    }

    @Override
    public List<Address> findAll() {

        return execute(connection -> {

            List<Address> addresses = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(AddressSql.FIND_ALL);

                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    addresses.add(mapRow(resultSet));
                }
            }

            LOGGER.info(
                    "Retrieved {} addresses",
                    addresses.size());

            return addresses;

        }, "Failed to retrieve addresses");
    }

    @Override
    public Address update(Address address) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(AddressSql.UPDATE)) {

                statement.setString(1, address.getCountry());
                statement.setString(2, address.getCity());
                statement.setString(3, address.getStreet());
                statement.setString(4, address.getHouseNumber());
                statement.setBigDecimal(5, address.getLatitude());
                statement.setBigDecimal(6, address.getLongitude());
                statement.setLong(7, address.getAddressId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException(
                            "Address not found with id=" + address.getAddressId());
                }

                LOGGER.info(
                        "Address updated successfully. ID={}",
                        address.getAddressId());

                return address;
            }

        }, "Error updating address with id=" + address.getAddressId());
    }

    @Override
    public void delete(Long addressId) {

        execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(AddressSql.DELETE)) {

                statement.setLong(1, addressId);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException(
                            "Address not found with id=" + addressId);
                }

                LOGGER.info(
                        "Address deleted successfully. ID={}",
                        addressId);

                return null;
            }

        }, "Failed to delete address with id=" + addressId);
    }

    @Override
    public List<Address> findByCountryAndCity(String country, String city) {

        return execute(connection -> {

            List<Address> addresses = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(AddressSql.FIND_BY_COUNTRY_AND_CITY)) {

                statement.setString(1, country);
                statement.setString(2, city);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        addresses.add(mapRow(resultSet));
                    }
                }
            }

            LOGGER.info(
                    "Found {} addresses for {}, {}",
                    addresses.size(),
                    country,
                    city);

            return addresses;

        }, "Failed to find addresses");
    }

    @Override
    public Address findByFullAddress(String country,
                                     String city,
                                     String street,
                                     String houseNumber) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(AddressSql.FIND_BY_FULL_ADDRESS)) {

                statement.setString(1, country);
                statement.setString(2, city);
                statement.setString(3, street);
                statement.setString(4, houseNumber);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        Address address = mapRow(resultSet);

                        LOGGER.info(
                                "Address found: {}, {}, {}, {}",
                                country,
                                city,
                                street,
                                houseNumber);

                        return address;
                    }

                    throw new DaoException(
                            "Address not found: "
                                    + country + ", "
                                    + city + ", "
                                    + street + ", "
                                    + houseNumber);
                }
            }

        }, "Failed to find address");
    }
}