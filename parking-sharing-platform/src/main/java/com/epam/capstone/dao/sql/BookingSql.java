package com.epam.capstone.dao.sql;

public final class BookingSql {

    private BookingSql() {
    }

    public static final String INSERT = """
            INSERT INTO bookings
            (
                driver_id,
                spot_id,
                start_time,
                end_time,
                total_price,
                status
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    public static final String FIND_BY_ID = """
            SELECT *
            FROM bookings
            WHERE booking_id = ?
            """;

    public static final String FIND_ALL = """
            SELECT *
            FROM bookings
            ORDER BY booking_id
            LIMIT ?
            OFFSET ?
            """;

    public static final String UPDATE = """
            UPDATE bookings
            SET
                driver_id = ?,
                spot_id = ?,
                start_time = ?,
                end_time = ?,
                total_price = ?,
                status = ?
            WHERE booking_id = ?
            """;

    public static final String DELETE = """
            DELETE
            FROM bookings
            WHERE booking_id = ?
            """;

    public static final String FIND_BY_DRIVER_ID = """
            SELECT *
            FROM bookings
            WHERE driver_id = ?
            ORDER BY booking_id
            LIMIT ?
            OFFSET ?
            """;

    public static final String FIND_BY_SPOT_ID = """
            SELECT *
            FROM bookings
            WHERE spot_id = ?
            ORDER BY booking_id
            LIMIT ?
            OFFSET ?
            """;

    public static final String FIND_BY_SPOT_ID_ALL = """
            SELECT *
            FROM bookings
            WHERE spot_id = ?
            ORDER BY start_time ASC
            """;

    public static final String FIND_BY_STATUS = """
            SELECT *
            FROM bookings
            WHERE status = ?
            ORDER BY booking_id
            LIMIT ?
            OFFSET ?
            """;

    public static final String UPDATE_STATUS = """
            UPDATE bookings
            SET status = ?
            WHERE booking_id = ?
            """;

    public static final String EXISTS_ACTIVE_BOOKING = """
            SELECT EXISTS(
                SELECT 1
                FROM bookings
                WHERE spot_id = ?
                  AND status IN ('PENDING', 'CONFIRMED')
                  AND start_time < ?
                  AND end_time > ?
            )
            """;
}