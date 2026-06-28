package com.epam.capstone.dao;

import com.epam.capstone.pool.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseDaoTest {

    protected static ConnectionPool pool;

    @BeforeAll
    static void initPool() {
        pool = ConnectionPool.getInstance();
    }
}
