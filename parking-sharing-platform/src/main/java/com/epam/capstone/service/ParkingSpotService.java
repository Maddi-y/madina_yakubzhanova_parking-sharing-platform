package com.epam.capstone.service;

import com.epam.capstone.model.ParkingSpot;

import java.util.List;

public interface ParkingSpotService {

    ParkingSpot create(ParkingSpot parkingSpot);

    ParkingSpot findById(Long id);

    List<ParkingSpot> findAll(int page, int size);

    ParkingSpot update(ParkingSpot parkingSpot);

    boolean deleteById(Long id);

    List<ParkingSpot> findByOwnerId(Long ownerId, int page, int size);

    List<ParkingSpot> findAvailable(int page, int size);
}
