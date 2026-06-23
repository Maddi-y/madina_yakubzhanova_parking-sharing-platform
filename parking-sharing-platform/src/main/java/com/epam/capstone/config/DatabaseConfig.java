package com.epam.capstone.config;

import com.epam.capstone.pool.ConnectionPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean
    public ConnectionPool connectionPool() {
        return ConnectionPool.getInstance();
    }
}
