package com.epam.capstone.dao.impl;

import com.epam.capstone.pool.ConnectionPool;

import java.sql.Connection;

public abstract class BaseDao {
    protected final ConnectionPool connectionPool;

    protected BaseDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    protected Connection getConnection() {
        return connectionPool.getConnection();
    }

    protected void releaseConnection(Connection connection) {
        connectionPool.releaseConnection(connection);
    }
}
