package com.epam.capstone.pool;

import com.epam.capstone.config.ApplicationProperties;
import com.epam.capstone.exception.ConnectionPoolException;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class ConnectionPool {

    private static final String URL = ApplicationProperties.getProperty("db.url");
    private static final String USERNAME = ApplicationProperties.getProperty("db.username");
    private static final String PASSWORD = ApplicationProperties.getProperty("db.password");
    private static final String DRIVER = ApplicationProperties.getProperty("db.driver");

    private static final int POOL_SIZE = Integer.parseInt(
            ApplicationProperties.getProperty("pool.size")
    );

    private static final ConnectionPool INSTANCE = new ConnectionPool();

    private final BlockingQueue<Connection> availableConnections;
    private final BlockingQueue<Connection> usedConnections;

    private volatile boolean closed = false;

    private ConnectionPool() {
        try {
            Class.forName(DRIVER);

            availableConnections = new ArrayBlockingQueue<>(POOL_SIZE);
            usedConnections = new ArrayBlockingQueue<>(POOL_SIZE);

            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = DriverManager.getConnection(
                        URL,
                        USERNAME,
                        PASSWORD
                );
                availableConnections.offer(connection);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new ConnectionPoolException(
                    "Failed to initialize connection pool",
                    e
            );
        }
    }

    public static ConnectionPool getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {

        if (closed) {
            throw new ConnectionPoolException("Connection pool is already closed");
        }

        try {
            Connection connection = availableConnections.take();
            usedConnections.offer(connection);

            return connection;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            throw new ConnectionPoolException(
                    "Failed to get connection from pool", e);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }

        usedConnections.remove(connection);
        availableConnections.offer(connection);
    }

    public synchronized void shutdown() {

        if (closed) {
            return;
        }

        closed = true;

        try {
            for (Connection connection : availableConnections) {
                connection.close();
            }

            for (Connection connection : usedConnections) {
                connection.close();
            }

            availableConnections.clear();
            usedConnections.clear();

        } catch (SQLException e) {
            throw new ConnectionPoolException(
                    "Failed to close connections", e);
        }
    }

    public int getAvailableConnectionsCount() {
        return availableConnections.size();
    }

    public int getUsedConnectionsCount() {
        return usedConnections.size();
    }
}
