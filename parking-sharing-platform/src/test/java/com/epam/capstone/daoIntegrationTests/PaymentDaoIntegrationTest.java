package com.epam.capstone.daoIntegrationTests;

import com.epam.capstone.dao.BookingDao;
import com.epam.capstone.dao.ParkingSpotDao;
import com.epam.capstone.dao.PaymentDao;
import com.epam.capstone.dao.UserDao;
import com.epam.capstone.dao.impl.BookingDaoImpl;
import com.epam.capstone.dao.impl.ParkingSpotDaoImpl;
import com.epam.capstone.dao.impl.PaymentDaoImpl;
import com.epam.capstone.dao.impl.UserDaoImpl;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.Payment;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.*;
import com.epam.capstone.pool.ConnectionPool;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDaoIntegrationTest {
    public static void main(String[] args) {

        ConnectionPool pool =
                ConnectionPool.getInstance();

        UserDao userDao =
                new UserDaoImpl(pool);

        ParkingSpotDao parkingSpotDao =
                new ParkingSpotDaoImpl(pool);

        BookingDao bookingDao =
                new BookingDaoImpl(pool);

        PaymentDao paymentDao =
                new PaymentDaoImpl(pool);

        User owner = new User(
                null,
                UserRole.OWNER,
                "Payment Owner",
                "payment.owner3@mail.com",
                "+77055659917",
                "hash123",
                UserStatus.ACTIVE,
                null
        );

        owner = userDao.save(owner);

        User driver = new User(
                null,
                UserRole.DRIVER,
                "Payment Driver",
                "payment.driver4@mail.com",
                "+77092272212",
                "hash123",
                UserStatus.ACTIVE,
                null
        );

        driver = userDao.save(driver);

        ParkingSpot spot =
                new ParkingSpot(
                        null,
                        owner,
                        "Payment Spot",
                        "Almaty",
                        "Test parking spot",
                        new BigDecimal("1500"),
                        ParkingSpotStatus.AVAILABLE,
                        new BigDecimal("43.238949"),
                        new BigDecimal("76.889709"),
                        null
                );

        spot = parkingSpotDao.save(spot);

        Booking booking =
                new Booking(
                        null,
                        driver,
                        spot,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2),
                        new BigDecimal("3000"),
                        BookingStatus.CONFIRMED,
                        null
                );

        booking = bookingDao.save(booking);

        Payment payment =
                new Payment(
                        null,
                        booking,
                        new BigDecimal("3000"),
                        PaymentMethod.CARD,
                        PaymentStatus.PENDING,
                        "TXN-" + System.currentTimeMillis(),
                        null
                );

        System.out.println("=== SAVE ===");

        payment = paymentDao.save(payment);

        System.out.println(payment);

        System.out.println();

        System.out.println("=== FIND BY ID ===");

        System.out.println(
                paymentDao.findById(
                        payment.getPaymentId()
                )
        );

        System.out.println();

        System.out.println("=== FIND BY BOOKING ID ===");

        System.out.println(
                paymentDao.findByBookingId(
                        booking.getBookingId()
                )
        );

        System.out.println();

        System.out.println("=== FIND BY TRANSACTION ID ===");

        System.out.println(
                paymentDao.findByTransactionId(
                        payment.getTransactionId()
                )
        );

        System.out.println();

        System.out.println("=== FIND BY STATUS ===");

        paymentDao.findByStatus(
                        PaymentStatus.PENDING,
                        1,
                        10
                )
                .forEach(System.out::println);

        System.out.println();

        System.out.println("=== FIND ALL ===");

        paymentDao.findAll(1, 10)
                .forEach(System.out::println);

        System.out.println();

        System.out.println("=== UPDATE ===");

        payment.setStatus(
                PaymentStatus.PAID
        );

        payment = paymentDao.update(payment);

        System.out.println(payment);

        System.out.println();

        System.out.println("=== DELETE ===");

        boolean deleted =
                paymentDao.deleteById(
                        payment.getPaymentId()
                );

        System.out.println(deleted);

        System.out.println();

        System.out.println("=== CHECK DELETED ===");

        System.out.println(
                paymentDao.findById(
                        payment.getPaymentId()
                )
        );
    }
}
