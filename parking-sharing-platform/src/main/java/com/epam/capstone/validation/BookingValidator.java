package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingValidator implements Validator<Booking> {

    @Override
    public void validate(Booking booking) {

        if (booking == null) {
            throw new ValidationException("Booking must not be null");
        }

        validateDriver(booking);
        validateParkingSpot(booking);
        validateBookingTime(booking.getStartTime(), booking.getEndTime());
        validateTotalPrice(booking.getTotalPrice());
        validateStatus(booking.getStatus());
    }

    private void validateDriver(Booking booking) {

        if (booking.getDriver() == null || booking.getDriver().getUserId() == null) {
            throw new ValidationException("Booking driver is required");
        }
    }

    private void validateParkingSpot(Booking booking) {

        if (booking.getParkingSpot() == null || booking.getParkingSpot().getSpotId() == null) {
            throw new ValidationException("Parking spot is required");
        }
    }

    private void validateBookingTime(LocalDateTime startTime, LocalDateTime endTime) {

        if (startTime == null) {
            throw new ValidationException("Start time is required");
        }

        if (endTime == null) {
            throw new ValidationException("End time is required");
        }

        if (!endTime.isAfter(startTime)) {
            throw new ValidationException("End time must be after start time");
        }
    }

    private void validateTotalPrice(BigDecimal totalPrice) {

        if (totalPrice == null) {
            throw new ValidationException("Total price is required");
        }

        if (totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Total price must be greater than zero");
        }
    }

    private void validateStatus(BookingStatus status) {

        if (status == null) {
            throw new ValidationException("Status is required");
        }
    }
}
