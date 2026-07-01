package com.epam.capstone.dao;

import com.epam.capstone.model.Parking;

import java.util.List;

public interface ParkingDao {

    Parking create(Parking parking);

    Parking findById(Long parkingId);

    List<Parking> findAll();

    Parking update(Parking parking);

    void delete(Long parkingId);

    List<Parking> findByOwnerId(Long ownerId);

    List<Parking> findByCountryAndCity(String country, String city);

    List<Parking> findByStreet(String country, String city, String street);
}
