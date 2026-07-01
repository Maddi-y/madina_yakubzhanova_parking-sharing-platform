package com.epam.capstone.dao;

import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.enums.ParkingSpotStatus;

import java.util.List;
import java.util.Optional;

public interface ParkingSpotDao extends GenericDao<ParkingSpot, Long>{

    List<ParkingSpot> findByParkingId(Long parkingId);

    List<ParkingSpot> findAvailableByParkingId(Long parkingId);

    boolean existsByParkingAndSpotNumber(Long parkingId, Integer spotNumber);

    boolean existsById(Long spotId);

    void updateStatus(Long spotId, ParkingSpotStatus status);
}
