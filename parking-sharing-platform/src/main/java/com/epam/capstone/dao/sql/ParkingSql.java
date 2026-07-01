package com.epam.capstone.dao.sql;

public final class ParkingSql {

    private ParkingSql() {
    }

    public static final String INSERT = """
        INSERT INTO parking
        (
            owner_id,
            address_id,
            title,
            description,
            hourly_rate
        )
        VALUES (?, ?, ?, ?, ?)
        """;

    public static final String FIND_BY_ID = """
        SELECT *
        FROM parking
        WHERE parking_id = ?
        """;

    public static final String FIND_ALL = """
        SELECT *
        FROM parking
        ORDER BY parking_id
        """;

    public static final String UPDATE = """
        UPDATE parking
        SET
            owner_id = ?,
            address_id = ?,
            title = ?,
            description = ?,
            hourly_rate = ?
        WHERE parking_id = ?
        """;

    public static final String DELETE = """
        DELETE
        FROM parking
        WHERE parking_id = ?
        """;

    public static final String FIND_BY_OWNER_ID = """
        SELECT *
        FROM parking
        WHERE owner_id = ?
        ORDER BY parking_id
        """;

    public static final String FIND_BY_COUNTRY_AND_CITY = """
        SELECT p.*
        FROM parking p
        JOIN address a
          ON p.address_id = a.address_id
        WHERE a.country = ?
          AND a.city = ?
        ORDER BY p.title
        """;

    public static final String FIND_BY_STREET = """
        SELECT p.*
        FROM parking p
        JOIN address a
          ON p.address_id = a.address_id
        WHERE a.country = ?
          AND a.city = ?
          AND a.street ILIKE CONCAT('%', ?, '%')
        ORDER BY p.title
        """;
}