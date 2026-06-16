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

    private Review mapRow(ResultSet resultSet) throws SQLException {

        Booking booking = new Booking();

        booking.setBookingId(resultSet.getLong("booking_id"));

        User author = new User();

        author.setUserId(resultSet.getLong("author_id"));

        return new Review(
                resultSet.getLong("review_id"),
                booking,
                author,
                resultSet.getInt("rating"),
                resultSet.getString("comment"),
                resultSet.getTimestamp("created_at")
                        .toLocalDateTime()
        );
    }

    @Override
    public Review save(Review entity) {

        if (entity.getBooking() == null || entity.getBooking().getBookingId() == null) {
            throw new DaoException("Review booking must not be null");
        }

        if (entity.getAuthor() == null || entity.getAuthor().getUserId() == null) {
            throw new DaoException("Review author must not be null");
        }

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(INSERT_REVIEW, Statement.RETURN_GENERATED_KEYS)) {

                statement.setLong(1, entity.getBooking().getBookingId());
                statement.setLong(2, entity.getAuthor().getUserId());
                statement.setInt(3, entity.getRating());
                statement.setString(4, entity.getComment());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Failed to save review");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {
                        entity.setReviewId(generatedKeys.getLong(1));
                    } else {
                        throw new DaoException("Failed to obtain generated review id");
                    }
                }

                LOGGER.info("Review saved successfully. ID={}", entity.getReviewId());
                return entity;
            }
        }, "Error saving review");
    }

    @Override
    public Optional<Review> findById(Long id) {

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

        }, "Error finding review by id");
    }

    @Override
    public List<Review> findAll(int page, int size) {

        return execute(connection -> {

            List<Review> reviews = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_ALL)) {

                statement.setInt(1, size);
                statement.setInt(2, calculateOffset(page, size));

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        reviews.add(mapRow(resultSet));
                    }
                }
                return reviews;
            }
        }, "Error finding all reviews");
    }

    @Override
    public Review update(Review entity) {

        return execute(connection -> {

            try (PreparedStatement statement =
                         connection.prepareStatement(UPDATE_REVIEW)) {

                statement.setInt(1, entity.getRating());
                statement.setString(2, entity.getComment());
                statement.setLong(3, entity.getReviewId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Review not found. ID=" + entity.getReviewId());
                }

                LOGGER.info("Review updated successfully. ID={}", entity.getReviewId());
                return entity;
            }
        }, "Error updating review");
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
                    LOGGER.info("Review deleted successfully. ID={}", id);
                }
                return deleted;
            }
        }, "Error deleting review. ID={}");
    }

    @Override
    public Optional<Review> findByBookingId(Long bookingId) {

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
        }, "Error finding review by bookingId");
    }

    @Override
    public List<Review> findByAuthorId(Long authorId, int page, int size) {

        return execute(connection -> {

            List<Review> reviews = new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(FIND_BY_AUTHOR_ID)) {

                statement.setLong(1, authorId);
                statement.setInt(2, size);
                statement.setInt(3, calculateOffset(page, size));

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        reviews.add(mapRow(resultSet));
                    }
                }
                return reviews;
            }
        }, "Error finding reviews by authorId");
    }
}
