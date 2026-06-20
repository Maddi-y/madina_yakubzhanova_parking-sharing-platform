package com.epam.capstone.daoIntegrationTests;

import com.epam.capstone.dao.BookingDao;
import com.epam.capstone.dao.ParkingSpotDao;
import com.epam.capstone.dao.ReviewDao;
import com.epam.capstone.dao.UserDao;
import com.epam.capstone.dao.impl.BookingDaoImpl;
import com.epam.capstone.dao.impl.ParkingSpotDaoImpl;
import com.epam.capstone.dao.impl.ReviewDaoImpl;
import com.epam.capstone.dao.impl.UserDaoImpl;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.Review;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.BookingStatus;
import com.epam.capstone.model.enums.ParkingSpotStatus;
import com.epam.capstone.model.enums.UserRole;
import com.epam.capstone.model.enums.UserStatus;
import com.epam.capstone.pool.ConnectionPool;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReviewDaoIntegrationTest {
    public static void main(String[] args) {

        ConnectionPool pool = ConnectionPool.getInstance();

        UserDao userDao = new UserDaoImpl(pool);

        ParkingSpotDao parkingSpotDao = new ParkingSpotDaoImpl(pool);

        BookingDao bookingDao = new BookingDaoImpl(pool);

        ReviewDao reviewDao = new ReviewDaoImpl(pool);

        /*
         * OWNER
         */
        User owner = new User(
                null,
                UserRole.OWNER,
                "Review Owner",
                "review.owner@mail.com",
                "+77001111111",
                "hash123",
                UserStatus.ACTIVE,
                null
        );

        owner = userDao.save(owner);

        /*
         * DRIVER
         */
        User driver = new User(
                null,
                UserRole.DRIVER,
                "Review Driver",
                "review.driver@mail.com",
                "+77002222222",
                "hash123",
                UserStatus.ACTIVE,
                null
        );

        driver = userDao.save(driver);

        /*
         * PARKING SPOT
         */
        ParkingSpot spot =
                new ParkingSpot(
                        null,
                        owner,
                        "Review Spot",
                        "Almaty",
                        "Test parking spot",
                        new BigDecimal("1000"),
                        ParkingSpotStatus.AVAILABLE,
                        new BigDecimal("43.238949"),
                        new BigDecimal("76.889709"),
                        null
                );

        spot = parkingSpotDao.save(spot);

        /*
         * BOOKING
         */
        Booking booking =
                new Booking(
                        null,
                        driver,
                        spot,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2),
                        new BigDecimal("2000"),
                        BookingStatus.CONFIRMED,
                        null
                );

        booking = bookingDao.save(booking);

        /*
         * REVIEW
         */
        Review review =
                new Review(
                        null,
                        booking,
                        driver,
                        5,
                        "Excellent parking spot",
                        null
                );

        System.out.println("=== SAVE ===");

        review = reviewDao.save(review);

        System.out.println(review);

        System.out.println();

        System.out.println("=== FIND BY ID ===");

        System.out.println(
                reviewDao.findById(
                        review.getReviewId()
                )
        );

        System.out.println();

        System.out.println("=== FIND BY BOOKING ID ===");

        System.out.println(
                reviewDao.findByBookingId(
                        booking.getBookingId()
                )
        );

        System.out.println();

        System.out.println("=== FIND BY AUTHOR ID ===");

        reviewDao.findByAuthorId(
                        driver.getUserId(),
                        1,
                        10
                )
                .forEach(System.out::println);

        System.out.println();

        System.out.println("=== FIND ALL ===");

        reviewDao.findAll(1, 10)
                .forEach(System.out::println);

        System.out.println();

        System.out.println("=== UPDATE ===");

        review.setRating(4);
        review.setComment(
                "Good parking spot"
        );

        review = reviewDao.update(review);

        System.out.println(review);

        System.out.println();

        System.out.println("=== DELETE ===");

        boolean deleted =
                reviewDao.deleteById(
                        review.getReviewId()
                );

        System.out.println(deleted);

        System.out.println();

        System.out.println("=== CHECK DELETED ===");

        System.out.println(
                reviewDao.findById(
                        review.getReviewId()
                )
        );
    }
}
