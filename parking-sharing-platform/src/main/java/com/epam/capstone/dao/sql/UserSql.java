package com.epam.capstone.dao.sql;

public final class UserSql {

    private UserSql() {

    }

    public static final String INSERT_USER = """
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

    public static final String FIND_BY_ID = """
            SELECT *
            FROM users
            WHERE user_id = ?
            """;

    public static final String FIND_BY_EMAIL = """
            SELECT *
            FROM users
            WHERE email = ?
            """;

    public static final String EXISTS_BY_EMAIL = """
            SELECT EXISTS (
                SELECT 1
                FROM users
                WHERE email = ?
            )
            """;

    public static final String EXISTS_BY_PHONE = """
            SELECT EXISTS(
                SELECT 1
                FROM users
                WHERE phone = ?
            )
            """;

    public static final String FIND_ALL = """
            SELECT *
            FROM users
            ORDER BY user_id
            LIMIT ?
            OFFSET ?
            """;

    public static final String UPDATE_USER = """
            UPDATE users
            SET role = ?,
                name = ?,
                email = ?,
                phone = ?,
                password_hash = ?,
                status = ?
            WHERE user_id = ?
            """;

    public static final String DELETE_BY_ID = """
            DELETE
            FROM users
            WHERE user_id = ?
            """;
}
