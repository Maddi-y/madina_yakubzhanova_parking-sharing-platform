package com.epam.capstone.dao;

import com.epam.capstone.model.Booking;
import com.epam.capstone.model.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingDao extends GenericDao<Booking, Long> {

    List<Booking> findByDriverId(Long driverId, int page, int size);

    List<Booking> findByParkingSpotId(Long spotId, int page, int size);

    List<Booking> findByParkingSpotId(Long spotId);

    List<Booking> findByStatus(BookingStatus status, int page, int size);

    boolean existsActiveBooking(Long spotId, LocalDateTime startTime, LocalDateTime endTime);

    void updateStatus(Long bookingId, BookingStatus status);
}
