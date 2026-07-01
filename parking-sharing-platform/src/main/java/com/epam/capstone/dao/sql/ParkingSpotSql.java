package com.epam.capstone.dao.sql;

public final class ParkingSpotSql {

    private ParkingSpotSql() {
    }

    public static final String INSERT = """
            INSERT INTO parking_spot
            (
                parking_id,
                spot_number,
                status
            )
            VALUES (?, ?, ?)
            """;

    public static final String FIND_BY_ID = """
            SELECT *
            FROM parking_spot
            WHERE spot_id = ?
            """;

    public static final String FIND_ALL = """
            SELECT *
            FROM parking_spot
            ORDER BY parking_id,
                     spot_number
            LIMIT ?
            OFFSET ?
            """;

    public static final String UPDATE = """
            UPDATE parking_spot
            SET
                parking_id = ?,
                spot_number = ?,
                status = ?
            WHERE spot_id = ?
            """;

    public static final String DELETE = """
            DELETE
            FROM parking_spot
            WHERE spot_id = ?
            """;

    public static final String FIND_BY_PARKING = """
            SELECT *
            FROM parking_spot
            WHERE parking_id = ?
            ORDER BY spot_number
            """;

    public static final String FIND_AVAILABLE_BY_PARKING = """
            SELECT *
            FROM parking_spot
            WHERE parking_id = ?
              AND status = 'AVAILABLE'
            ORDER BY spot_number
            """;

    public static final String EXISTS_BY_PARKING_AND_NUMBER = """
            SELECT EXISTS(
                SELECT 1
                FROM parking_spot
                WHERE parking_id = ?
                  AND spot_number = ?
            )
            """;

    public static final String EXISTS_BY_ID = """
        SELECT EXISTS(
            SELECT 1
            FROM parking_spot
            WHERE spot_id = ?
        )
        """;

    public static final String UPDATE_STATUS = """
        UPDATE parking_spot
        SET status = ?
        WHERE spot_id = ?
        """;

    public static final String COUNT_BY_PARKING = """
        SELECT COUNT(*)
        FROM parking_spot
        WHERE parking_id = ?
        """;
}
