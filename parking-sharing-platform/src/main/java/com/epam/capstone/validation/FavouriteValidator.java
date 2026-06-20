package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Favourite;

public class FavouriteValidator implements Validator<Favourite> {

    @Override
    public void validate(Favourite favourite) {

        if (favourite == null) {
            throw new ValidationException("Favourite must not be null");
        }

        validateUser(favourite);
        validateParkingSpot(favourite);
    }

    private void validateUser(Favourite favourite) {

        if (favourite.getUser() == null || favourite.getUser().getUserId() == null) {
            throw new ValidationException("User is required");
        }
    }

    private void validateParkingSpot(Favourite favourite) {

        if (favourite.getParkingSpot() == null || favourite.getParkingSpot().getSpotId() == null) {
            throw new ValidationException("Parking spot is required");
        }
    }
}
