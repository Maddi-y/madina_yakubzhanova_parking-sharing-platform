package com.epam.capstone.dao;

import com.epam.capstone.model.Address;

import java.util.List;

public interface AddressDao {

    Address create(Address address);

    Address findById(Long addressId);

    List<Address> findAll();

    Address update(Address address);

    void delete(Long addressId);

    List<Address> findByCountryAndCity(String country, String city);

    Address findByFullAddress(String country, String city, String street, String houseNumber);
}
