package com.epam.capstone.dao.impl;

import com.epam.capstone.dao.PaymentDao;
import com.epam.capstone.exception.DaoException;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.Payment;
import com.epam.capstone.model.enums.PaymentMethod;
import com.epam.capstone.model.enums.PaymentStatus;
import com.epam.capstone.pool.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentDaoImpl extends BaseDao implements PaymentDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentDaoImpl.class);

    private static final String INSERT_PAYMENT = """
            INSERT INTO payments (
                booking_id,
                amount,
                payment_method,
                status,
                transaction_id
            )
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String FIND_BY_ID = """
            SELECT *
            FROM payments
            WHERE payment_id = ?
            """;

    private static final String FIND_ALL = """
            SELECT *
            FROM payments
            ORDER BY payment_id
            LIMIT ?
            OFFSET ?
            """;

    private static final String UPDATE_PAYMENT = """
            UPDATE payments
            SET status = ?
            WHERE payment_id = ?
            """;

    private static final String DELETE_BY_ID = """
            DELETE
            FROM payments
            WHERE payment_id = ?
            """;

    private static final String FIND_BY_BOOKING_ID = """
            SELECT *
            FROM payments
            WHERE booking_id = ?
            """;

    private static final String FIND_BY_TRANSACTION_ID = """
            SELECT *
            FROM payments
            WHERE transaction_id = ?
            """;

    private static final String FIND_BY_STATUS = """
            SELECT *
            FROM payments
            WHERE status = ?
            ORDER BY payment_id
            LIMIT ?
            OFFSET ?
            """;

    public PaymentDaoImpl(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private Payment mapRow(ResultSet resultSet) throws SQLException {

        Booking booking = new Booking();

        booking.setBookingId(resultSet.getLong("booking_id"));

        return new Payment(
                resultSet.getLong("payment_id"),
                booking,
                resultSet.getBigDecimal("amount"),
                PaymentMethod.valueOf(resultSet.getString("payment_method")),
                PaymentStatus.valueOf(resultSet.getString("status")),
                resultSet.getString("transaction_id"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Override
    public Payment save(Payment entity) {

        if (entity.getAmount() == null || entity.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DaoException("Payment amount must be greater than zero");
        }

        if (entity.getBooking() == null || entity.getBooking().getBookingId() == null) {
            throw new DaoException("Payment booking must not be null");
        }

        if (entity.getTransactionId() == null || entity.getTransactionId().isBlank()) {
            throw new DaoException("Transaction id must not be null");
        }

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(INSERT_PAYMENT, Statement.RETURN_GENERATED_KEYS)) {

                statement.setLong(1, entity.getBooking().getBookingId());
                statement.setBigDecimal(2, entity.getAmount());
                statement.setString(3, entity.getPaymentMethod().name());
                statement.setString(4, entity.getStatus().name());
                statement.setString(5, entity.getTransactionId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Failed to save payment");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {
                        entity.setPaymentId(generatedKeys.getLong(1));
                    } else {
                        throw new DaoException("Failed to obtain generated payment id");
                    }
                }

                LOGGER.info("Payment saved successfully. ID={}", entity.getPaymentId());
                return entity;
            }
        }, "Error saving payment");
    }

    @Override
    public Optional<Payment> findById(Long id) {

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

        }, "Error finding payment by id");
    }

    @Override
    public List<Payment> findAll(int page, int size) {

        return execute(connection -> {

            List<Payment> payments = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_ALL)) {

                statement.setInt(1, size);
                statement.setInt(2, calculateOffset(page, size));

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        payments.add(mapRow(resultSet));
                    }
                }
                return payments;
            }

        }, "Error finding all payments");
    }

    @Override
    public Payment update(Payment entity) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(UPDATE_PAYMENT)) {

                statement.setString(1, entity.getStatus().name());
                statement.setLong(2, entity.getPaymentId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Payment not found. ID=" + entity.getPaymentId());
                }

                LOGGER.info("Payment updated successfully. ID={}", entity.getPaymentId());
                return entity;
            }

        }, "Error updating payment");
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
                    LOGGER.info("Payment deleted successfully. ID={}", id);
                }
                return deleted;
            }

        }, "Error deleting payment");
    }

    @Override
    public Optional<Payment> findByBookingId(Long bookingId) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_BY_BOOKING_ID)) {

                statement.setLong(1, bookingId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {
                        return Optional.of(mapRow(resultSet));
                    }
                    return Optional.empty();
                }
            }

        }, "Error finding payment by booking id");
    }

    @Override
    public Optional<Payment> findByTransactionId(String transactionId) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_BY_TRANSACTION_ID)) {

                statement.setString(1, transactionId);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {
                        return Optional.of(mapRow(resultSet));
                    }
                    return Optional.empty();
                }
            }
        }, "Error finding payment by transaction id");
    }

    @Override
    public List<Payment> findByStatus(PaymentStatus status, int page, int size) {

        return execute(connection -> {

            List<Payment> payments = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_BY_STATUS)) {

                statement.setString(1, status.name());
                statement.setInt(2, size);
                statement.setInt(3, calculateOffset(page, size));

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        payments.add(mapRow(resultSet));
                    }
                }
                return payments;
            }

        }, "Error finding payments by status");
    }
}
