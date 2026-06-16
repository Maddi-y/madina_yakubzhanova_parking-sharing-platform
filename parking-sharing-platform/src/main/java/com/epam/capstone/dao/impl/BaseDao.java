package com.epam.capstone.dao.impl;

import com.epam.capstone.exception.DaoException;
import com.epam.capstone.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDao {

    protected final ConnectionPool connectionPool;

    protected BaseDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    protected Connection getConnection() {
        return connectionPool.getConnection();
    }

    protected void releaseConnection(Connection connection) {
        if (connection != null) {
            connectionPool.releaseConnection(connection);
        }
    }

    protected int calculateOffset(int page, int size) {
        return (page - 1) * size;
    }

    protected DaoException daoError(String message, Throwable e) {
        return new DaoException(message, e);
    }

    protected DaoException daoError(String message) {
        return new DaoException(message);
    }

    @FunctionalInterface
    protected interface SqlFunction<T> {
        T apply(Connection connection) throws SQLException;
    }

    protected <T> T execute(
            SqlFunction<T> function,
            String errorMessage
    ) {

        Connection connection = null;

        try {

            connection = getConnection();

            return function.apply(connection);

        } catch (SQLException e) {

            throw new DaoException(
                    errorMessage,
                    e
            );

        } finally {

            releaseConnection(connection);
        }
    }
}
