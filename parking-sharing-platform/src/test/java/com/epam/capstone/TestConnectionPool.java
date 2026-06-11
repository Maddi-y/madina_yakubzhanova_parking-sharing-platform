package com.epam.capstone;

import com.epam.capstone.pool.ConnectionPool;

import java.sql.Connection;

public class TestConnectionPool {

    public static void main(String[] args) {

        ConnectionPool pool =
                ConnectionPool.getInstance();

        System.out.println(
                "Available: "
                        + pool.getAvailableConnectionsCount()
        );

        Connection connection =
                pool.getConnection();

        System.out.println(
                "Available after get: "
                        + pool.getAvailableConnectionsCount()
        );

        pool.releaseConnection(connection);

        System.out.println(
                "Available after release: "
                        + pool.getAvailableConnectionsCount()
        );
    }
}
