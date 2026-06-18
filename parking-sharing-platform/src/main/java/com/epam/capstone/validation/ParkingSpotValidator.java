package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.ParkingSpot;

import java.math.BigDecimal;

public class ParkingSpotValidator implements Validator<ParkingSpot> {

    @Override
    public void validate(ParkingSpot parkingSpot) {

        if (parkingSpot == null) {
            throw new ValidationException("Parking spot must not be null");
        }

        validateOwner(parkingSpot);

        validateTitle(parkingSpot.getTitle());

        validateAddress(parkingSpot.getAddress());

        validateHourlyRate(parkingSpot.getHourlyRate());

        validateCoordinates(parkingSpot.getLatitude(), parkingSpot.getLongitude());

        validateStatus(parkingSpot);
    }

    private void validateOwner(ParkingSpot parkingSpot) {

        if (parkingSpot.getOwner() == null || parkingSpot.getOwner().getUserId() == null) {
            throw new ValidationException("Parking spot owner is required");
        }
    }

    private void validateTitle(String title) {

        if (title == null || title.isBlank()) {
            throw new ValidationException("Title is required");
        }

        if (title.length() > 100) {
            throw new ValidationException("Title must not exceed 100 characters");
        }
    }

    private void validateAddress(String address) {

        if (address == null || address.isBlank()) {
            throw new ValidationException("Address is required");
        }

        if (address.length() > 255) {
            throw new ValidationException("Address must not exceed 255 characters");
        }
    }

    private void validateHourlyRate(BigDecimal hourlyRate) {

        if (hourlyRate == null) {
            throw new ValidationException("Hourly rate is required");
        }

        if (hourlyRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Hourly rate must be greater than zero");
        }
    }

    private void validateCoordinates(BigDecimal latitude, BigDecimal longitude) {

        if (latitude == null || longitude == null) {
            throw new ValidationException("Coordinates are required");
        }

        if (latitude.compareTo(BigDecimal.valueOf(-90)) < 0 || latitude.compareTo(BigDecimal.valueOf(90)) > 0) {
            throw new ValidationException("Latitude must be between -90 and 90");
        }

        if (longitude.compareTo(BigDecimal.valueOf(-180)) < 0 || longitude.compareTo(BigDecimal.valueOf(180)) > 0) {
            throw new ValidationException("Longitude must be between -180 and 180");
        }
    }

    private void validateStatus(ParkingSpot parkingSpot) {

        if (parkingSpot.getStatus() == null) {
            throw new ValidationException("Status is required");
        }
    }
}
