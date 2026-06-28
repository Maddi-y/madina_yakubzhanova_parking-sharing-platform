package com.epam.capstone.dao.impl;

import com.epam.capstone.dao.UserDao;
import com.epam.capstone.exception.DaoException;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.UserRole;
import com.epam.capstone.model.enums.UserStatus;
import com.epam.capstone.pool.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl extends BaseDao implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    private static final String INSERT_USER = """
            INSERT INTO users (
                role,
                name,
                email,
                phone,
                password_hash,
                status
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String FIND_BY_ID = """
            SELECT *
            FROM users
            WHERE user_id = ?
            """;

    private static final String FIND_BY_EMAIL = """
            SELECT *
            FROM users
            WHERE email = ?
            """;

    private static final String EXISTS_BY_EMAIL = """
            SELECT EXISTS (
                SELECT 1
                FROM users
                WHERE email = ?
            )
            """;

    private static final String EXISTS_BY_PHONE = """
        SELECT EXISTS(
            SELECT 1
            FROM users
            WHERE phone = ?
        )
        """;

    private static final String FIND_ALL = """
            SELECT *
            FROM users
            ORDER BY user_id
            LIMIT ?
            OFFSET ?
            """;

    private static final String UPDATE_USER = """
            UPDATE users
            SET role = ?,
                name = ?,
                email = ?,
                phone = ?,
                password_hash = ?,
                status = ?
            WHERE user_id = ?
            """;

    private static final String DELETE_BY_ID = """
            DELETE
            FROM users
            WHERE user_id = ?
            """;

    public UserDaoImpl(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private User mapRow(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("user_id"),
                UserRole.valueOf(resultSet.getString("role")),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("phone"),
                resultSet.getString("password_hash"),
                UserStatus.valueOf(resultSet.getString("status")),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Override
    public User save(User entity) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, entity.getRole().name());
                statement.setString(2, entity.getName());
                statement.setString(3, entity.getEmail());
                statement.setString(4, entity.getPhone());
                statement.setString(5, entity.getPassword());
                statement.setString(6, entity.getStatus().name());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Failed to save user");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setUserId(generatedKeys.getLong(1));

                        LOGGER.info("User saved successfully. ID={}", entity.getUserId());
                        return entity;
                    }
                }

                throw new DaoException("Failed to retrieve generated user id");
            }
        }, "Error saving user with email=" + entity.getEmail());
    }

    @Override
    public Optional<User> findById(Long id) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_BY_ID)) {

                statement.setLong(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {

                        User user = mapRow(resultSet);

                        LOGGER.info("User found by id={}", id);
                        return Optional.of(user);
                    }

                    LOGGER.info("User not found by id={}", id);
                    return Optional.empty();
                }
            }
        }, "Failed to find user by id: " + id);
    }

    @Override
    public List<User> findAll(int page, int size) {

        return execute(connection -> {

            List<User> users = new ArrayList<>();
            int offset = calculateOffset(page, size);

            try (PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {

                statement.setInt(1, size);
                statement.setInt(2, offset);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        users.add(mapRow(resultSet));
                    }
                }
            }
            LOGGER.info("Retrieved {} users. Page={}, Size={}", users.size(), page, size);
            return users;

        }, "Failed to retrieve users");
    }


    @Override
    public User update(User entity) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER)) {

                statement.setString(1, entity.getRole().name());
                statement.setString(2, entity.getName());
                statement.setString(3, entity.getEmail());
                statement.setString(4, entity.getPhone());
                statement.setString(5, entity.getPassword());
                statement.setString(6, entity.getStatus().name());
                statement.setLong(7, entity.getUserId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("User not found with id: " + entity.getUserId());
                }

                LOGGER.info("User updated successfully. ID={}", entity.getUserId());
                return entity;
            }
        }, "Error updating user with id=" + entity.getUserId());
    }

    @Override
    public boolean deleteById(Long id) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {

                statement.setLong(1, id);

                int affectedRows = statement.executeUpdate();

                boolean deleted = affectedRows > 0;

                if (deleted) {
                    LOGGER.info("User deleted successfully. ID={}", id);
                } else {
                    LOGGER.info("User not found for deletion. ID={}", id);
                }

                return deleted;
            }
        }, "Failed to delete user with id=" + id);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL)) {

                statement.setString(1, email);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {
                        User user = mapRow(resultSet);

                        LOGGER.info("User found by email={}", email);
                        return Optional.of(user);
                    }

                    LOGGER.info("User not found by email={}", email);
                    return Optional.empty();
                }
            }
        }, "Failed to find user by email: " + email);
    }

    @Override
    public boolean existsByEmail(String email) {

        return execute(connection -> {

            try (PreparedStatement statement = connection.prepareStatement(EXISTS_BY_EMAIL)) {

                statement.setString(1, email);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {
                        boolean exists = resultSet.getBoolean(1);

                        LOGGER.info("Email existence check for {}: {}", email, exists);
                        return exists;
                    }

                    return false;
                }
            }
        }, "Failed to check email existence: " + email);
    }

    @Override
    public boolean existsByPhone(String phone) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(EXISTS_BY_PHONE)) {

                statement.setString(1, phone);

                try (ResultSet rs = statement.executeQuery()) {

                    if (rs.next()) {
                        return rs.getBoolean(1);
                    }

                    return false;
                }
            }
        }, "Failed to check phone existence");
    }
}
