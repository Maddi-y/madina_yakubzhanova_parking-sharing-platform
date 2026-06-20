package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingValidatorTest {

    private BookingValidator validator;

    @BeforeEach
    void setUp() {
        validator = new BookingValidator();
    }

    private Booking createValidBooking() {

        User driver = new User();
        driver.setUserId(1L);

        ParkingSpot spot = new ParkingSpot();
        spot.setSpotId(10L);

        Booking booking = new Booking();
        booking.setDriver(driver);
        booking.setParkingSpot(spot);
        booking.setStartTime(LocalDateTime.now().plusHours(1));
        booking.setEndTime(LocalDateTime.now().plusHours(3));
        booking.setTotalPrice(BigDecimal.valueOf(2000));
        booking.setStatus(BookingStatus.PENDING);

        return booking;
    }

    @Test
    void validate_ShouldPass_WhenBookingIsValid() {

        Booking booking = createValidBooking();

        assertDoesNotThrow(() -> validator.validate(booking));
    }

    @Test
    void validate_ShouldThrowException_WhenBookingIsNull() {

        ValidationException ex = assertThrows(ValidationException.class, () ->
                validator.validate(null)
        );

        assertEquals("Booking must not be null", ex.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenDriverIsNull() {

        Booking booking = createValidBooking();
        booking.setDriver(null);

        assertThrows(ValidationException.class, () -> validator.validate(booking)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenParkingSpotIsNull() {

        Booking booking = createValidBooking();
        booking.setParkingSpot(null);

        assertThrows(ValidationException.class, () -> validator.validate(booking)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenStartTimeIsNull() {

        Booking booking = createValidBooking();
        booking.setStartTime(null);

        assertThrows(ValidationException.class, () -> validator.validate(booking)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenEndTimeIsNull() {

        Booking booking = createValidBooking();
        booking.setEndTime(null);

        assertThrows(ValidationException.class, () -> validator.validate(booking)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenEndTimeBeforeStartTime() {

        Booking booking = createValidBooking();
        booking.setStartTime(LocalDateTime.now().plusHours(3));
        booking.setEndTime(LocalDateTime.now().plusHours(1));

        assertThrows(ValidationException.class, () -> validator.validate(booking)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenTotalPriceIsZero() {

        Booking booking = createValidBooking();
        booking.setTotalPrice(BigDecimal.ZERO);

        assertThrows(ValidationException.class, () -> validator.validate(booking)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenStatusIsNull() {

        Booking booking = createValidBooking();
        booking.setStatus(null);

        assertThrows(ValidationException.class, () -> validator.validate(booking)
        );
    }
}