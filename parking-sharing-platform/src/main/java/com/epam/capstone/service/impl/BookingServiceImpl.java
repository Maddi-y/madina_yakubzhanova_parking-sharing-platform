package com.epam.capstone.service.impl;

import com.epam.capstone.dao.BookingDao;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.enums.BookingStatus;
import com.epam.capstone.service.BookingService;
import com.epam.capstone.validation.BookingValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingDao bookingDao;
    private final BookingValidator bookingValidator;

    public BookingServiceImpl(BookingDao bookingDao, BookingValidator bookingValidator) {
        this.bookingDao = bookingDao;
        this.bookingValidator = bookingValidator;
    }

    @Override
    public Booking create(Booking booking) {

        if (booking == null) {
            throw new ValidationException("Booking must not be null");
        }

        bookingValidator.validate(booking);

        if (!booking.getStartTime().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Start time must be in the future");
        }

        validateBookingOverlap(booking);

        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        Booking savedBooking = bookingDao.save(booking);

        LOGGER.info("Booking created successfully. ID={}", savedBooking.getBookingId());

        return savedBooking;
    }

    @Override
    public Booking findById(Long id) {

        if (id == null || id <= 0) {
            throw new ValidationException("Invalid booking id");
        }

        return bookingDao.findById(id).orElseThrow(() -> {
            LOGGER.warn("Booking not found. ID={}", id);
            return new ServiceException("Booking not found");
        });
    }

    @Override
    public List<Booking> findAll(int page, int size) {

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return bookingDao.findAll(page, size);
    }

    @Override
    public Booking update(Booking booking) {

        if (booking == null) {
            throw new ValidationException("Booking must not be null");
        }

        if (booking.getBookingId() == null) {
            throw new ValidationException("Booking id is required");
        }

        bookingValidator.validate(booking);

        Booking existingBooking = findById(booking.getBookingId());

        if (existingBooking.getStatus() == BookingStatus.CANCELLED) {
            LOGGER.warn("Attempt to update cancelled booking. ID={}", booking.getBookingId());

            throw new ValidationException("Cancelled booking cannot be updated");
        }

        if (existingBooking.getStatus() == BookingStatus.COMPLETED) {
            LOGGER.warn("Attempt to update completed booking. ID={}", booking.getBookingId());

            throw new ValidationException("Completed booking cannot be updated");
        }

        if (!booking.getStartTime().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Start time must be in the future");
        }

        validateBookingOverlap(booking);

        Booking updatedBooking = bookingDao.update(booking);

        LOGGER.info("Booking updated successfully. ID={}", updatedBooking.getBookingId());

        return updatedBooking;
    }

    @Override
    public boolean cancel(Long bookingId) {

        if (bookingId == null || bookingId <= 0) {
            throw new ValidationException("Invalid booking id");
        }

        Booking booking = findById(bookingId);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            LOGGER.warn("Booking already cancelled. ID={}", bookingId);
            return false;
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            LOGGER.warn("Attempt to cancel completed booking. ID={}", bookingId);

            throw new ValidationException("Completed booking cannot be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        bookingDao.update(booking);

        LOGGER.info("Booking cancelled successfully. ID={}", bookingId);

        return true;
    }

    @Override
    public List<Booking> findByDriverId(Long driverId, int page, int size) {

        if (driverId == null || driverId <= 0) {
            throw new ValidationException("Invalid driver id");
        }

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return bookingDao.findByDriverId(driverId, page, size);
    }

    @Override
    public List<Booking> findByParkingSpotId(Long spotId, int page, int size) {

        if (spotId == null || spotId <= 0) {
            throw new ValidationException("Invalid parking spot id");
        }

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return bookingDao.findByParkingSpotId(spotId, page, size
        );
    }

    @Override
    public List<Booking> findByStatus(BookingStatus status, int page, int size) {

        if (status == null) {
            throw new ValidationException("Booking status is required");
        }

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return bookingDao.findByStatus(status, page, size);
    }

    private void validateBookingOverlap(Booking booking) {

        List<Booking> existingBookings = bookingDao.findByParkingSpotId(booking.getParkingSpot().getSpotId());

        for (Booking existing : existingBookings) {
            if (booking.getBookingId() != null && existing.getBookingId().equals(booking.getBookingId())) {
                continue;
            }

            if (existing.getStatus() == BookingStatus.CANCELLED) {
                continue;
            }

            boolean overlaps = booking.getStartTime().isBefore(existing.getEndTime()) &&
                    booking.getEndTime().isAfter(existing.getStartTime());

            if (overlaps) {
                throw new ValidationException("Parking spot is already booked for selected time");
            }
        }
    }
}
