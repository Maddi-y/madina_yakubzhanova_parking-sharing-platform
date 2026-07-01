package com.epam.capstone.dao.sql;

public final class PaymentSql {

    private PaymentSql() {

    }

    public static final String INSERT_PAYMENT = """
            INSERT INTO payments (
                booking_id,
                amount,
                payment_method,
                status,
                transaction_id
            )
            VALUES (?, ?, ?, ?, ?)
            """;

    public static final String FIND_BY_ID = """
            SELECT *
            FROM payments
            WHERE payment_id = ?
            """;

    public static final String FIND_ALL = """
            SELECT *
            FROM payments
            ORDER BY created_at DESC
            LIMIT ?
            OFFSET ?
            """;

    public static final String DELETE_BY_ID = """
            DELETE
            FROM payments
            WHERE payment_id = ?
            """;

    public static final String FIND_BY_BOOKING_ID = """
            SELECT *
            FROM payments
            WHERE booking_id = ?
            """;

    public static final String FIND_BY_TRANSACTION_ID = """
            SELECT *
            FROM payments
            WHERE transaction_id = ?
            """;

    public static final String FIND_BY_STATUS = """
            SELECT *
            FROM payments
            WHERE status = ?
            ORDER BY created_at DESC
            LIMIT ?
            OFFSET ?
            """;

    public static final String UPDATE_STATUS = """
        UPDATE payments
        SET status = ?
        WHERE payment_id = ?
        """;
}
