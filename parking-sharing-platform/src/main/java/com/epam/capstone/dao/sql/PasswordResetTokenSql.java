package com.epam.capstone.dao.sql;

public final class PasswordResetTokenSql {

    public static final String INSERT_TOKEN = """
            INSERT INTO password_reset_token
            (
                user_id,
                token,
                expires_at,
                created_at
            )
            VALUES (?, ?, ?, ?)
            """;

    public static final String FIND_BY_TOKEN = """
            SELECT *
            FROM password_reset_token
            WHERE token = ?
            """;

    public static final String DELETE_BY_TOKEN = """
            DELETE
            FROM password_reset_token
            WHERE token = ?
            """;

    public static final String DELETE_BY_USER_ID = """
            DELETE
            FROM password_reset_token
            WHERE user_id = ?
            """;
}
