package com.epam.capstone;

import com.epam.capstone.dao.FavouriteDao;
import com.epam.capstone.dao.ParkingSpotDao;
import com.epam.capstone.dao.UserDao;
import com.epam.capstone.dao.impl.FavouriteDaoImpl;
import com.epam.capstone.dao.impl.ParkingSpotDaoImpl;
import com.epam.capstone.dao.impl.UserDaoImpl;
import com.epam.capstone.model.Favourite;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.ParkingSpotStatus;
import com.epam.capstone.model.enums.UserRole;
import com.epam.capstone.model.enums.UserStatus;
import com.epam.capstone.pool.ConnectionPool;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class FavouriteDaoIntegrationTest {
    public static void main(String[] args) {

        ConnectionPool pool =
                ConnectionPool.getInstance();

        UserDao userDao =
                new UserDaoImpl(pool);

        ParkingSpotDao parkingSpotDao =
                new ParkingSpotDaoImpl(pool);

        FavouriteDao favouriteDao =
                new FavouriteDaoImpl(pool);

        User owner = new User(
                null,
                UserRole.OWNER,
                "Favourite Owner",
                "fav.owner@mail.com",
                "+77001110000",
                "hash123",
                UserStatus.ACTIVE,
                null
        );

        owner = userDao.save(owner);

        ParkingSpot spot =
                new ParkingSpot(
                        null,
                        owner,
                        "Favourite Spot",
                        "Almaty",
                        "Test parking spot",
                        new BigDecimal("1500"),
                        ParkingSpotStatus.AVAILABLE,
                        new BigDecimal("43.238949"),
                        new BigDecimal("76.889709"),
                        null
                );

        spot = parkingSpotDao.save(spot);

        Favourite favourite =
                new Favourite(
                        null,
                        owner,
                        spot,
                        null
                );

        System.out.println("=== SAVE ===");

        favourite = favouriteDao.save(favourite);

        System.out.println(favourite);

        System.out.println();
        System.out.println("=== FIND BY ID ===");

        Optional<Favourite> found =
                favouriteDao.findById(
                        favourite.getFavouriteId()
                );

        System.out.println(found);

        System.out.println();
        System.out.println("=== FIND ALL ===");

        List<Favourite> favourites =
                favouriteDao.findAll(1, 10);

        favourites.forEach(System.out::println);

        System.out.println();
        System.out.println("=== FIND BY USER ID ===");

        favouriteDao.findByUserId(
                        owner.getUserId(),
                        1,
                        10
                )
                .forEach(System.out::println);

        System.out.println();
        System.out.println("=== EXISTS ===");

        System.out.println(
                favouriteDao.exists(
                        owner.getUserId(),
                        spot.getSpotId()
                )
        );

        System.out.println();
        System.out.println("=== UPDATE ===");

        favourite = favouriteDao.update(
                favourite
        );

        System.out.println(favourite);

        System.out.println();
        System.out.println("=== DELETE ===");

        boolean deleted =
                favouriteDao.deleteById(
                        favourite.getFavouriteId()
                );

        System.out.println(deleted);

        System.out.println();
        System.out.println("=== CHECK DELETED ===");

        System.out.println(
                favouriteDao.findById(
                        favourite.getFavouriteId()
                )
        );
    }
}
