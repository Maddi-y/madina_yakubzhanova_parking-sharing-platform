package com.epam.capstone.dao;

import com.epam.capstone.dao.impl.BookingDaoImpl;
import com.epam.capstone.dao.impl.ParkingSpotDaoImpl;
import com.epam.capstone.dao.impl.PaymentDaoImpl;
import com.epam.capstone.dao.impl.UserDaoImpl;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.Payment;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDaoTest extends BaseDaoTest {

    private UserDao userDao;
    private ParkingSpotDao parkingSpotDao;
    private BookingDao bookingDao;
    private PaymentDao paymentDao;

    private TestData data;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(pool);
        parkingSpotDao = new ParkingSpotDaoImpl(pool);
        bookingDao = new BookingDaoImpl(pool);
        paymentDao = new PaymentDaoImpl(pool);

        data = null;
    }

    @AfterEach
    void tearDown() {
        if (data == null) return;

        try {
            if (data.payment != null) {
                paymentDao.deleteById(data.payment.getPaymentId());
            }
        } catch (Exception ignored) {}

        try {
            if (data.booking != null) {
                bookingDao.deleteById(data.booking.getBookingId());
            }
        } catch (Exception ignored) {}

        try {
            if (data.spot != null) {
                parkingSpotDao.deleteById(data.spot.getSpotId());
            }
        } catch (Exception ignored) {}

        try {
            if (data.driver != null) {
                userDao.deleteById(data.driver.getUserId());
            }
        } catch (Exception ignored) {}

        try {
            if (data.owner != null) {
                userDao.deleteById(data.owner.getUserId());
            }
        } catch (Exception ignored) {}
    }

    private static class TestData {
        User owner;
        User driver;
        ParkingSpot spot;
        Booking booking;
        Payment payment;
    }

    private TestData createScenario() {
        TestData data = new TestData();

        data.owner = userDao.save(new User(
                null,
                UserRole.OWNER,
                "Payment Owner",
                "owner_" + UUID.randomUUID() + "@mail.com",
                "+77075114517",
                "hash123",
                UserStatus.ACTIVE,
                LocalDateTime.now()
        ));

        data.driver = userDao.save(new User(
                null,
                UserRole.DRIVER,
                "Payment Driver",
                "driver_" + UUID.randomUUID() + "@mail.com",
                "+77000889212",
                "hash123",
                UserStatus.ACTIVE,
                LocalDateTime.now()
        ));

        data.spot = parkingSpotDao.save(new ParkingSpot(
                null,
                data.owner,
                "Payment Spot",
                "Almaty",
                "Test parking spot",
                new BigDecimal("1500"),
                ParkingSpotStatus.AVAILABLE,
                new BigDecimal("43.238949"),
                new BigDecimal("76.889709"),
                null
        ));

        data.booking = bookingDao.save(new Booking(
                null,
                data.driver,
                data.spot,
                LocalDateTime.now().plusMinutes(10),
                LocalDateTime.now().plusHours(2),
                new BigDecimal("3000"),
                BookingStatus.CONFIRMED,
                null
        ));

        data.payment = paymentDao.save(new Payment(
                null,
                data.booking,
                new BigDecimal("3000"),
                PaymentMethod.CARD,
                PaymentStatus.PENDING,
                "TXN-" + System.currentTimeMillis(),
                null
        ));

        return data;
    }

    @Test
    void save_ShouldCreatePayment() {
        data = createScenario();

        assertNotNull(data.payment.getPaymentId());
        assertEquals(PaymentStatus.PENDING, data.payment.getStatus());
    }

    @Test
    void findById_ShouldReturnPayment() {
        data = createScenario();

        var result = paymentDao.findById(data.payment.getPaymentId());

        assertTrue(result.isPresent());
    }

    @Test
    void findByBookingId_ShouldReturnPayment() {
        data = createScenario();

        var result = paymentDao.findByBookingId(data.booking.getBookingId());

        assertNotNull(result);
    }

    @Test
    void findByTransactionId_ShouldReturnPayment() {
        data = createScenario();

        var result = paymentDao.findByTransactionId(data.payment.getTransactionId());

        assertTrue(result.isPresent());
    }

    @Test
    void findByStatus_ShouldReturnPayments() {
        data = createScenario();

        var result = paymentDao.findByStatus(PaymentStatus.PENDING, 1, 10);

        assertFalse(result.isEmpty());
    }

    @Test
    void findAll_ShouldReturnPayments() {
        data = createScenario();

        var result = paymentDao.findAll(1, 10);

        assertFalse(result.isEmpty());
    }

    @Test
    void update_ShouldChangeStatus() {
        data = createScenario();

        data.payment.setStatus(PaymentStatus.PAID);

        Payment updated = paymentDao.update(data.payment);

        assertEquals(PaymentStatus.PAID, updated.getStatus());
    }

    @Test
    void deleteById_ShouldRemovePayment() {
        data = createScenario();

        boolean deleted =
                paymentDao.deleteById(data.payment.getPaymentId());

        assertTrue(deleted);
        assertTrue(paymentDao.findById(data.payment.getPaymentId()).isEmpty());

        data.payment = null; // чтобы tearDown не пытался удалить второй раз
    }
}