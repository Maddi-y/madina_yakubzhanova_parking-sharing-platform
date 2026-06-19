package com.epam.capstone.service;

import com.epam.capstone.model.Booking;
import com.epam.capstone.model.enums.BookingStatus;

import java.util.List;

public interface BookingService {

    Booking create(Booking booking);

    Booking findById(Long id);

    List<Booking> findAll(int page, int size);

    Booking update(Booking booking);

    boolean cancel(Long bookingId);

    List<Booking> findByDriverId(Long driverId, int page, int size);

    List<Booking> findByParkingSpotId(Long spotId, int page, int size);

    List<Booking> findByStatus(BookingStatus status, int page, int size);
}