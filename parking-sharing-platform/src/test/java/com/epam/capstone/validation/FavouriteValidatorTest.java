package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Favourite;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FavouriteValidatorTest {

    private FavouriteValidator validator;

    @BeforeEach
    void setUp() {
        validator = new FavouriteValidator();
    }

    private Favourite createValidFavourite() {

        User user = new User();
        user.setUserId(1L);

        ParkingSpot spot = new ParkingSpot();
        spot.setSpotId(10L);

        Favourite favourite = new Favourite();
        favourite.setUser(user);
        favourite.setParkingSpot(spot);

        return favourite;
    }

    @Test
    void validate_ShouldPass_WhenDataIsValid() {

        Favourite favourite = createValidFavourite();

        assertDoesNotThrow(() -> validator.validate(favourite));
    }

    @Test
    void validate_ShouldThrowException_WhenFavouriteIsNull() {

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(null)
        );

        assertEquals("Favourite must not be null", ex.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenUserIsNull() {

        Favourite favourite = createValidFavourite();
        favourite.setUser(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(favourite)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenUserIdIsNull() {

        Favourite favourite = createValidFavourite();
        favourite.getUser().setUserId(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(favourite)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenParkingSpotIsNull() {

        Favourite favourite = createValidFavourite();
        favourite.setParkingSpot(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(favourite)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenSpotIdIsNull() {

        Favourite favourite = createValidFavourite();
        favourite.getParkingSpot().setSpotId(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(favourite)
        );
    }
}
