package com.epam.capstone.config;

import com.epam.capstone.exception.ConnectionPoolException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream =
                     ApplicationProperties.class
                             .getClassLoader()
                             .getResourceAsStream("application.properties")) {

            if (inputStream == null) {
                throw new ConnectionPoolException(
                        "application.properties not found"
                );
            }

            PROPERTIES.load(inputStream);

        } catch (IOException e) {
            throw new ConnectionPoolException(
                    "Failed to load application.properties",
                    e
            );
        }
    }

    private ApplicationProperties() {
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}

