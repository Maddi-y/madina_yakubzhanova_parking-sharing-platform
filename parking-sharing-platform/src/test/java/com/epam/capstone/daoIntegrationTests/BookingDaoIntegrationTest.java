package com.epam.capstone.daoIntegrationTests;

import com.epam.capstone.dao.BookingDao;
import com.epam.capstone.dao.ParkingSpotDao;
import com.epam.capstone.dao.UserDao;
import com.epam.capstone.dao.impl.BookingDaoImpl;
import com.epam.capstone.dao.impl.ParkingSpotDaoImpl;
import com.epam.capstone.dao.impl.UserDaoImpl;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.BookingStatus;
import com.epam.capstone.model.enums.ParkingSpotStatus;
import com.epam.capstone.model.enums.UserRole;
import com.epam.capstone.model.enums.UserStatus;
import com.epam.capstone.pool.ConnectionPool;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BookingDaoIntegrationTest {
    public static void main(String[] args) {

        ConnectionPool pool =
                ConnectionPool.getInstance();

        UserDao userDao =
                new UserDaoImpl(pool);

        ParkingSpotDao parkingSpotDao =
                new ParkingSpotDaoImpl(pool);

        BookingDao bookingDao =
                new BookingDaoImpl(pool);

        User driver = new User(
                null,
                UserRole.DRIVER,
                "Test Driver",
                "driver8@mail.com",
                "+77053158246",
                "hash123",
                UserStatus.ACTIVE,
                null
        );

        driver = userDao.save(driver);

        User owner = new User(
                null,
                UserRole.OWNER,
                "Test Owner",
                "owner6@mail.com",
                "+77082463344",
                "hash123",
                UserStatus.ACTIVE,
                null
        );

        owner = userDao.save(owner);

        ParkingSpot parkingSpot =
                new ParkingSpot(
                        null,
                        owner,
                        "Parking A",
                        "Almaty",
                        "Test spot",
                        new BigDecimal("1000"),
                        ParkingSpotStatus.AVAILABLE,
                        new BigDecimal("43.238949"),
                        new BigDecimal("76.889709"),
                        null
                );

        parkingSpot =
                parkingSpotDao.save(parkingSpot);

        Booking booking =
                new Booking(
                        null,
                        driver,
                        parkingSpot,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2),
                        new BigDecimal("2000"),
                        BookingStatus.PENDING,
                        null
                );

        System.out.println("=== SAVE ===");

        booking = bookingDao.save(booking);

        System.out.println(booking);

        System.out.println();
        System.out.println("=== FIND BY ID ===");

        Optional<Booking> found =
                bookingDao.findById(
                        booking.getBookingId()
                );

        System.out.println(found);

        System.out.println();
        System.out.println("=== FIND ALL ===");

        List<Booking> bookings =
                bookingDao.findAll(1, 10);

        bookings.forEach(System.out::println);

        System.out.println();
        System.out.println("=== FIND BY DRIVER ===");

        bookingDao.findByDriverId(
                        driver.getUserId(),
                        1,
                        10
                )
                .forEach(System.out::println);

        System.out.println();
        System.out.println("=== FIND BY SPOT ===");

        bookingDao.findByParkingSpotId(
                        parkingSpot.getSpotId(),
                        1,
                        10
                )
                .forEach(System.out::println);

        System.out.println();
        System.out.println("=== FIND BY STATUS ===");

        bookingDao.findByStatus(
                        BookingStatus.PENDING,
                        1,
                        10
                )
                .forEach(System.out::println);

        System.out.println();
        System.out.println("=== UPDATE ===");

        booking.setStatus(
                BookingStatus.CONFIRMED
        );

        bookingDao.update(booking);

        System.out.println(
                bookingDao.findById(
                        booking.getBookingId()
                )
        );

        System.out.println();
        System.out.println("=== DELETE ===");

        boolean deleted =
                bookingDao.deleteById(
                        booking.getBookingId()
                );

        System.out.println(deleted);

        System.out.println();
        System.out.println("=== CHECK DELETED ===");

        System.out.println(
                bookingDao.findById(
                        booking.getBookingId()
                )
        );
    }
}
