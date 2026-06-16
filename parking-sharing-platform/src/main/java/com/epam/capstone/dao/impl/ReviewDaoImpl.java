package com.epam.capstone.dao.impl;

import com.epam.capstone.dao.ReviewDao;
import com.epam.capstone.exception.DaoException;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.Review;
import com.epam.capstone.model.User;
import com.epam.capstone.pool.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDaoImpl extends BaseDao implements ReviewDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewDaoImpl.class);

    private static final String INSERT_REVIEW = """
            INSERT INTO reviews (
                booking_id,
                author_id,
                rating,
                comment
            )
            VALUES (?, ?, ?, ?)
            """;

    private static final String FIND_BY_ID = """
            SELECT *
            FROM reviews
            WHERE review_id = ?
            """;

    private static final String FIND_ALL = """
            SELECT *
            FROM reviews
            ORDER BY review_id
            LIMIT ?
            OFFSET ?
            """;

    private static final String UPDATE_REVIEW = """
            UPDATE reviews
            SET rating = ?,
                comment = ?
            WHERE review_id = ?
            """;

    private static final String DELETE_BY_ID = """
            DELETE
            FROM reviews
            WHERE review_id = ?
            """;

    private static final String FIND_BY_BOOKING_ID = """
            SELECT *
            FROM reviews
            WHERE booking_id = ?
            """;

    private static final String FIND_BY_AUTHOR_ID = """
            SELECT *
            FROM reviews
            WHERE author_id = ?
            ORDER BY review_id
            LIMIT ?
            OFFSET ?
            """;

    public ReviewDaoImpl(ConnectionPool connectionPool) {
        super(connectionPool);
    }

    private Review mapRow(ResultSet rs)
            throws SQLException {

        Booking booking = new Booking();

        booking.setBookingId(
                rs.getLong("booking_id")
        );

        User author = new User();

        author.setUserId(
                rs.getLong("author_id")
        );

        return new Review(
                rs.getLong("review_id"),
                booking,
                author,
                rs.getInt("rating"),
                rs.getString("comment"),
                rs.getTimestamp("created_at")
                        .toLocalDateTime()
        );
    }

    @Override
    public Review save(Review entity) {

        if (entity.getBooking() == null
                || entity.getBooking().getBookingId() == null) {

            throw new DaoException(
                    "Review booking must not be null"
            );
        }

        if (entity.getAuthor() == null
                || entity.getAuthor().getUserId() == null) {

            throw new DaoException(
                    "Review author must not be null"
            );
        }

        Connection connection = null;

        try {

            connection = getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 INSERT_REVIEW,
                                 Statement.RETURN_GENERATED_KEYS
                         )) {

                statement.setLong(
                        1,
                        entity.getBooking().getBookingId()
                );

                statement.setLong(
                        2,
                        entity.getAuthor().getUserId()
                );

                statement.setInt(
                        3,
                        entity.getRating()
                );

                statement.setString(
                        4,
                        entity.getComment()
                );

                int affectedRows =
                        statement.executeUpdate();

                if (affectedRows == 0) {

                    throw new DaoException(
                            "Failed to save review"
                    );
                }

                try (ResultSet generatedKeys =
                             statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {

                        entity.setReviewId(
                                generatedKeys.getLong(1)
                        );

                    } else {

                        throw new DaoException(
                                "Failed to obtain generated review id"
                        );
                    }
                }

                LOGGER.info(
                        "Review saved successfully. ID={}",
                        entity.getReviewId()
                );

                return entity;
            }

        } catch (SQLException e) {

            LOGGER.error(
                    "Error saving review",
                    e
            );

            throw new DaoException(
                    "Failed to save review",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    @Override
    public Optional<Review> findById(Long id) {

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
                    "Error finding review by id={}",
                    id,
                    e
            );

            throw new DaoException(
                    "Failed to find review by id",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    @Override
    public List<Review> findAll(
            int page,
            int size
    ) {

        List<Review> reviews =
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

                        reviews.add(
                                mapRow(resultSet)
                        );
                    }
                }

                return reviews;
            }

        } catch (SQLException e) {

            LOGGER.error(
                    "Error finding all reviews",
                    e
            );

            throw new DaoException(
                    "Failed to find all reviews",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    @Override
    public Review update(Review entity) {

        Connection connection = null;

        try {

            connection = getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 UPDATE_REVIEW
                         )) {

                statement.setInt(
                        1,
                        entity.getRating()
                );

                statement.setString(
                        2,
                        entity.getComment()
                );

                statement.setLong(
                        3,
                        entity.getReviewId()
                );

                int affectedRows =
                        statement.executeUpdate();

                if (affectedRows == 0) {

                    throw new DaoException(
                            "Review not found. ID="
                                    + entity.getReviewId()
                    );
                }

                LOGGER.info(
                        "Review updated successfully. ID={}",
                        entity.getReviewId()
                );

                return entity;
            }

        } catch (SQLException e) {

            LOGGER.error(
                    "Error updating review. ID={}",
                    entity.getReviewId(),
                    e
            );

            throw new DaoException(
                    "Failed to update review",
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
                            "Review deleted successfully. ID={}",
                            id
                    );
                }

                return deleted;
            }

        } catch (SQLException e) {

            LOGGER.error(
                    "Error deleting review. ID={}",
                    id,
                    e
            );

            throw new DaoException(
                    "Failed to delete review",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    @Override
    public Optional<Review> findByBookingId(
            Long bookingId
    ) {

        Connection connection = null;

        try {

            connection = getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 FIND_BY_BOOKING_ID
                         )) {

                statement.setLong(
                        1,
                        bookingId
                );

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
                    "Error finding review by bookingId={}",
                    bookingId,
                    e
            );

            throw new DaoException(
                    "Failed to find review by booking id",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    @Override
    public List<Review> findByAuthorId(
            Long authorId,
            int page,
            int size
    ) {

        List<Review> reviews =
                new ArrayList<>();

        int offset = (page - 1) * size;

        Connection connection = null;

        try {

            connection = getConnection();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 FIND_BY_AUTHOR_ID
                         )) {

                statement.setLong(
                        1,
                        authorId
                );

                statement.setInt(
                        2,
                        size
                );

                statement.setInt(
                        3,
                        offset
                );

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    while (resultSet.next()) {

                        reviews.add(
                                mapRow(resultSet)
                        );
                    }
                }

                return reviews;

            }

        } catch (SQLException e) {

            LOGGER.error(
                    "Error finding reviews by authorId={}",
                    authorId,
                    e
            );

            throw new DaoException(
                    "Failed to find reviews by author id",
                    e
            );

        } finally {

            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

}
