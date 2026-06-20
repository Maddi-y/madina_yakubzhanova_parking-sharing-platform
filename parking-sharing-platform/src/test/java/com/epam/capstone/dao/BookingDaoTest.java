package com.epam.capstone.dao;

import com.epam.capstone.dao.impl.BookingDaoImpl;
import com.epam.capstone.dao.impl.ParkingSpotDaoImpl;
import com.epam.capstone.dao.impl.UserDaoImpl;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookingDaoTest extends BaseDaoTest {

    private UserDao userDao;
    private ParkingSpotDao parkingSpotDao;
    private BookingDao bookingDao;

    private TestData data;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(pool);
        parkingSpotDao = new ParkingSpotDaoImpl(pool);
        bookingDao = new BookingDaoImpl(pool);
        data = null;
    }

    @AfterEach
    void tearDown() {
        if (data == null) return;

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
        User driver;
        User owner;
        ParkingSpot spot;
        Booking booking;
    }

    private TestData createScenario() {
        TestData data = new TestData();

        data.driver = userDao.save(new User(
                null,
                UserRole.DRIVER,
                "Driver",
                "driver_" + UUID.randomUUID() + "@mail.com",
                "+77012338246",
                "hash",
                UserStatus.ACTIVE,
                LocalDateTime.now()
        ));

        data.owner = userDao.save(new User(
                null,
                UserRole.OWNER,
                "Owner",
                "owner_" + UUID.randomUUID() + "@mail.com",
                "+77003899885",
                "hash",
                UserStatus.ACTIVE,
                LocalDateTime.now()
        ));

        data.spot = parkingSpotDao.save(new ParkingSpot(
                null,
                data.owner,
                "Parking A",
                "Almaty",
                "Test spot",
                new BigDecimal("1000"),
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
                new BigDecimal("2000"),
                BookingStatus.PENDING,
                null
        ));

        return data;
    }

    @Test
    void save_ShouldCreateBooking() {
        data = createScenario();

        assertNotNull(data.booking.getBookingId());
        assertEquals(BookingStatus.PENDING, data.booking.getStatus());
    }

    @Test
    void findById_ShouldReturnBooking() {
        data = createScenario();

        Optional<Booking> result = bookingDao.findById(data.booking.getBookingId());

        assertTrue(result.isPresent());
    }

    @Test
    void update_ShouldChangeStatus() {
        data = createScenario();

        data.booking.setStatus(BookingStatus.CONFIRMED);

        Booking updated = bookingDao.update(data.booking);

        assertEquals(BookingStatus.CONFIRMED, updated.getStatus());
    }

    @Test
    void deleteById_ShouldRemoveBooking() {
        data = createScenario();

        boolean deleted = bookingDao.deleteById(data.booking.getBookingId());

        assertTrue(deleted);
        assertTrue(bookingDao.findById(data.booking.getBookingId()).isEmpty());

        data.booking = null;
    }
}