package com.epam.capstone.dao.sql;

public final class ReviewSql {

    private ReviewSql() {

    }

    public static final String INSERT_REVIEW = """
            INSERT INTO reviews (
                booking_id,
                author_id,
                rating,
                comment
            )
            VALUES (?, ?, ?, ?)
            """;

    public static final String FIND_BY_ID = """
            SELECT *
            FROM reviews
            WHERE review_id = ?
            """;

    public static final String FIND_ALL = """
            SELECT *
            FROM reviews
            ORDER BY created_at DESC
            LIMIT ?
            OFFSET ?
            """;

    public static final String UPDATE_REVIEW = """
            UPDATE reviews
            SET rating = ?,
                comment = ?
            WHERE review_id = ?
            """;

    public static final String DELETE_BY_ID = """
            DELETE
            FROM reviews
            WHERE review_id = ?
            """;

    public static final String FIND_BY_BOOKING_ID = """
            SELECT *
            FROM reviews
            WHERE booking_id = ?
            """;

    public static final String FIND_BY_AUTHOR_ID = """
            SELECT *
            FROM reviews
            WHERE author_id = ?
            ORDER BY created_at DESC
            LIMIT ?
            OFFSET ?
            """;

    public static final String FIND_BY_PARKING_ID = """
            SELECT r.*
            FROM reviews r
                     JOIN bookings b
                          ON r.booking_id = b.booking_id
                     JOIN parking_spot ps
                          ON b.spot_id = ps.spot_id
            WHERE ps.parking_id = ?
            ORDER BY r.created_at DESC
            LIMIT ?
            OFFSET ?
            """;
}
