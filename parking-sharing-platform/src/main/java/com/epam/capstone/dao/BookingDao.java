package com.epam.capstone.dao;

import com.epam.capstone.model.Booking;
import com.epam.capstone.model.enums.BookingStatus;

import java.util.List;

public interface BookingDao extends GenericDao<Booking, Long> {

    List<Booking> findByDriverId(
            Long driverId,
            int page,
            int size
    );

    List<Booking> findByParkingSpotId(
            Long spotId,
            int page,
            int size
    );

    List<Booking> findByStatus(
            BookingStatus status,
            int page,
            int size
    );
}
