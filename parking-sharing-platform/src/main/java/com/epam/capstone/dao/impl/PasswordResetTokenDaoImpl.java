package com.epam.capstone.dao.impl;

import com.epam.capstone.dao.PasswordResetTokenDao;

import com.epam.capstone.exception.DaoException;
import com.epam.capstone.model.PasswordResetToken;
import com.epam.capstone.pool.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

import static com.epam.capstone.dao.sql.PasswordResetTokenSql.*;


@Repository
public class PasswordResetTokenDaoImpl extends BaseDao implements PasswordResetTokenDao {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PasswordResetTokenDaoImpl.class);

    public PasswordResetTokenDaoImpl(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private PasswordResetToken mapRow(ResultSet rs)
            throws SQLException {

        return new PasswordResetToken(
                rs.getLong("token_id"),
                rs.getLong("user_id"),
                rs.getString("token"),
                rs.getTimestamp("expires_at").toLocalDateTime(),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Override
    public PasswordResetToken save(PasswordResetToken entity) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(INSERT_TOKEN, Statement.RETURN_GENERATED_KEYS)) {

                statement.setLong(1, entity.getUserId());
                statement.setString(2, entity.getToken());
                statement.setTimestamp(3, Timestamp.valueOf(entity.getExpiresAt()));
                statement.setTimestamp(4, Timestamp.valueOf(entity.getCreatedAt()));

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Failed to save reset token");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {
                        entity.setTokenId(generatedKeys.getLong(1));

                        LOGGER.info("Password reset token saved. ID={}", entity.getTokenId());

                        return entity;
                    }
                }

                throw new DaoException("Failed to retrieve generated token id");
            }

        }, "Error saving password reset token");
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_TOKEN)) {

                statement.setString(1, token);

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        PasswordResetToken resetToken = mapRow(rs);

                        LOGGER.info("Password reset token found");

                        return Optional.of(resetToken);
                    }

                    LOGGER.info("Password reset token not found");

                    return Optional.empty();
                }
            }

        }, "Failed to find password reset token");
    }

    @Override
    public void deleteByToken(String token) {

        execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_TOKEN)) {

                statement.setString(1, token);
                statement.executeUpdate();

                LOGGER.info("Password reset token deleted");

                return null;
            }

        }, "Failed to delete password reset token");
    }

    @Override
    public void deleteByUserId(Long userId) {

        execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_USER_ID)) {

                statement.setLong(1, userId);
                statement.executeUpdate();

                LOGGER.info("Old password reset tokens deleted. User={}", userId);

                return null;
            }

        }, "Failed to delete old reset tokens");
    }


}
