package com.epam.capstone.dao;

import com.epam.capstone.model.ParkingSpot;

import java.util.List;

public interface ParkingSpotDao extends GenericDao<ParkingSpot, Long>{
    List<ParkingSpot> findByOwnerId(
            Long ownerId,
            int page,
            int size
    );

    List<ParkingSpot> findAvailable(
            int page,
            int size
    );
}
