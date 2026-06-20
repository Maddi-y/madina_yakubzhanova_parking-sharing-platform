package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import com.epam.capstone.model.enums.ParkingSpotStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotValidatorTest {

    private ParkingSpotValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ParkingSpotValidator();
    }

    private ParkingSpot createValidSpot() {

        User owner = new User();
        owner.setUserId(1L);

        ParkingSpot spot = new ParkingSpot();
        spot.setOwner(owner);
        spot.setTitle("Valid Parking");
        spot.setAddress("Almaty, Abay 10");
        spot.setHourlyRate(BigDecimal.valueOf(1000));
        spot.setLatitude(BigDecimal.valueOf(43.238949));
        spot.setLongitude(BigDecimal.valueOf(76.889709));
        spot.setStatus(ParkingSpotStatus.AVAILABLE);

        return spot;
    }

    @Test
    void validate_ShouldPass_WhenDataIsValid() {

        ParkingSpot spot = createValidSpot();

        assertDoesNotThrow(() -> validator.validate(spot));
    }

    @Test
    void validate_ShouldThrowException_WhenSpotIsNull() {

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(null));

        assertEquals("Parking spot must not be null", ex.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenOwnerIsNull() {

        ParkingSpot spot = createValidSpot();
        spot.setOwner(null);

        assertThrows(ValidationException.class, () -> validator.validate(spot));
    }

    @Test
    void validate_ShouldThrowException_WhenTitleIsBlank() {

        ParkingSpot spot = createValidSpot();
        spot.setTitle(" ");

        assertThrows(ValidationException.class, () -> validator.validate(spot));
    }

    @Test
    void validate_ShouldThrowException_WhenAddressIsTooLong() {

        ParkingSpot spot = createValidSpot();
        spot.setAddress("a".repeat(300));

        assertThrows(ValidationException.class, () -> validator.validate(spot));
    }

    @Test
    void validate_ShouldThrowException_WhenHourlyRateIsZero() {

        ParkingSpot spot = createValidSpot();
        spot.setHourlyRate(BigDecimal.ZERO);

        assertThrows(ValidationException.class, () -> validator.validate(spot));
    }

    @Test
    void validate_ShouldThrowException_WhenLatitudeInvalid() {

        ParkingSpot spot = createValidSpot();
        spot.setLatitude(BigDecimal.valueOf(200));

        assertThrows(ValidationException.class, () -> validator.validate(spot));
    }

    @Test
    void validate_ShouldThrowException_WhenLongitudeInvalid() {

        ParkingSpot spot = createValidSpot();
        spot.setLongitude(BigDecimal.valueOf(300));

        assertThrows(ValidationException.class, () -> validator.validate(spot));
    }

    @Test
    void validate_ShouldThrowException_WhenStatusIsNull() {

        ParkingSpot spot = createValidSpot();
        spot.setStatus(null);

        assertThrows(ValidationException.class, () -> validator.validate(spot));
    }
}
