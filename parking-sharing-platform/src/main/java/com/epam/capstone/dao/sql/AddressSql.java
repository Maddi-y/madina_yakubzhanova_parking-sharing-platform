package com.epam.capstone.dao.sql;

public final class AddressSql {

    private AddressSql() {
    }

    public static final String INSERT = """
            INSERT INTO address
            (
                country,
                city,
                street,
                house_number,
                latitude,
                longitude
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    public static final String FIND_BY_ID = """
            SELECT *
            FROM address
            WHERE address_id = ?
            """;

    public static final String FIND_ALL = """
            SELECT *
            FROM address
            ORDER BY country,
                     city,
                     street,
                     house_number
            """;

    public static final String UPDATE = """
            UPDATE address
            SET
                country = ?,
                city = ?,
                street = ?,
                house_number = ?,
                latitude = ?,
                longitude = ?
            WHERE address_id = ?
            """;

    public static final String DELETE = """
            DELETE
            FROM address
            WHERE address_id = ?
            """;

    public static final String FIND_BY_COUNTRY_AND_CITY = """
            SELECT *
            FROM address
            WHERE country = ?
              AND city = ?
            ORDER BY street,
                     house_number
            """;

    public static final String FIND_BY_FULL_ADDRESS = """
            SELECT *
            FROM address
            WHERE country = ?
              AND city = ?
              AND street = ?
              AND house_number = ?
            """;
}