package com.epam.capstone.dao;

import com.epam.capstone.dao.impl.BookingDaoImpl;
import com.epam.capstone.dao.impl.ParkingSpotDaoImpl;
import com.epam.capstone.dao.impl.ReviewDaoImpl;
import com.epam.capstone.dao.impl.UserDaoImpl;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.Review;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReviewDaoTest extends BaseDaoTest {

    private UserDao userDao;
    private ParkingSpotDao parkingSpotDao;
    private BookingDao bookingDao;
    private ReviewDao reviewDao;

    private TestData data;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(pool);
        parkingSpotDao = new ParkingSpotDaoImpl(pool);
        bookingDao = new BookingDaoImpl(pool);
        reviewDao = new ReviewDaoImpl(pool);

        data = null;
    }

    @AfterEach
    void tearDown() {
        if (data == null) return;

        try {
            if (data.review != null) {
                reviewDao.deleteById(data.review.getReviewId());
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
        Review review;
    }

    private TestData createScenario() {
        TestData data = new TestData();

        data.owner = userDao.save(new User(
                null,
                UserRole.OWNER,
                "Review Owner",
                "owner_" + UUID.randomUUID() + "@mail.com",
                "+77001141338",
                "hash123",
                UserStatus.ACTIVE,
                LocalDateTime.now()
        ));

        data.driver = userDao.save(new User(
                null,
                UserRole.DRIVER,
                "Review Driver",
                "driver_" + UUID.randomUUID() + "@mail.com",
                "+77018625222",
                "hash123",
                UserStatus.ACTIVE,
                LocalDateTime.now()
        ));

        data.spot = parkingSpotDao.save(new ParkingSpot(
                null,
                data.owner,
                "Review Spot",
                "Almaty",
                "Test parking spot",
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
                BookingStatus.CONFIRMED,
                null
        ));

        data.review = reviewDao.save(new Review(
                null,
                data.booking,
                data.driver,
                5,
                "Excellent parking spot",
                null
        ));

        return data;
    }

    @Test
    void save_ShouldCreateReview() {
        data = createScenario();

        assertNotNull(data.review.getReviewId());
        assertEquals(5, data.review.getRating());
    }

    @Test
    void findById_ShouldReturnReview() {
        data = createScenario();

        var result = reviewDao.findById(data.review.getReviewId());

        assertTrue(result.isPresent());
    }

    @Test
    void findByBookingId_ShouldReturnReview() {
        data = createScenario();

        var result = reviewDao.findByBookingId(data.booking.getBookingId());

        assertNotNull(result);
    }

    @Test
    void findByAuthorId_ShouldReturnReviews() {
        data = createScenario();

        var result = reviewDao.findByAuthorId(data.driver.getUserId(), 1, 10);

        assertFalse(result.isEmpty());
    }

    @Test
    void findAll_ShouldReturnReviews() {
        data = createScenario();

        var result = reviewDao.findAll(1, 10);

        assertFalse(result.isEmpty());
    }

    @Test
    void update_ShouldChangeRatingAndComment() {
        data = createScenario();

        data.review.setRating(4);
        data.review.setComment("Good parking spot");

        Review updated = reviewDao.update(data.review);

        assertEquals(4, updated.getRating());
        assertEquals("Good parking spot", updated.getComment());
    }

    @Test
    void deleteById_ShouldRemoveReview() {
        data = createScenario();

        boolean deleted = reviewDao.deleteById(data.review.getReviewId());

        assertTrue(deleted);
        assertTrue(reviewDao.findById(data.review.getReviewId()).isEmpty());

        data.review = null;
    }
}