package com.epam.capstone.dao;

import com.epam.capstone.dao.impl.ParkingSpotDaoImpl;
import com.epam.capstone.dao.impl.UserDaoImpl;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.ParkingSpotStatus;
import com.epam.capstone.model.enums.UserRole;
import com.epam.capstone.model.enums.UserStatus;
import com.epam.capstone.pool.ConnectionPool;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParkingSpotDaoIntegrationTest {

    public static void main(String[] args) {

        ConnectionPool pool =
                ConnectionPool.getInstance();

        UserDao userDao =
                new UserDaoImpl(pool);

        ParkingSpotDao parkingSpotDao =
                new ParkingSpotDaoImpl(pool);

        // создаем владельца парковки

        User owner = new User(
                null,
                UserRole.OWNER,
                "Parking Owner",
                "owner@test.com",
                "+77001112233",
                "hashedPassword",
                UserStatus.ACTIVE,
                LocalDateTime.now()
        );

        owner = userDao.save(owner);

        // создаем парковочное место

        ParkingSpot parkingSpot =
                new ParkingSpot(
                        null,
                        owner,
                        "Test Parking",
                        "Almaty, Abay 10",
                        "Covered parking",
                        new BigDecimal("500"),
                        ParkingSpotStatus.AVAILABLE,
                        new BigDecimal("43.238949"),
                        new BigDecimal("76.889709"),
                        null
                );

        System.out.println("\n=== SAVE ===");

        parkingSpot = parkingSpotDao.save(parkingSpot);

        System.out.println(parkingSpot);

        System.out.println("\n=== FIND BY ID ===");

        System.out.println(
                parkingSpotDao.findById(
                        parkingSpot.getSpotId()
                )
        );

        System.out.println("\n=== FIND ALL ===");

        parkingSpotDao.findAll(1, 10)
                .forEach(System.out::println);

        System.out.println("\n=== FIND BY OWNER ===");

        parkingSpotDao.findByOwnerId(
                        owner.getUserId(),
                        1,
                        10
                )
                .forEach(System.out::println);

        System.out.println("\n=== FIND AVAILABLE ===");

        parkingSpotDao.findAvailable(
                        1,
                        10
                )
                .forEach(System.out::println);

        System.out.println("\n=== UPDATE ===");

        parkingSpot.setTitle(
                "Updated Parking"
        );

        parkingSpotDao.update(parkingSpot);

        System.out.println(
                parkingSpotDao.findById(
                        parkingSpot.getSpotId()
                )
        );

        System.out.println("\n=== DELETE ===");

        System.out.println(
                parkingSpotDao.deleteById(
                        parkingSpot.getSpotId()
                )
        );

        System.out.println("\n=== CHECK DELETED ===");

        System.out.println(
                parkingSpotDao.findById(
                        parkingSpot.getSpotId()
                )
        );

        pool.shutdown();
    }
}
