package com.epam.capstone.service.impl;

import com.epam.capstone.dao.ParkingSpotDao;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.ParkingSpot;
import com.epam.capstone.model.enums.ParkingSpotStatus;
import com.epam.capstone.service.ParkingSpotService;
import com.epam.capstone.validation.ParkingSpotValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ParkingSpotServiceImpl implements ParkingSpotService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingSpotServiceImpl.class);
    private final ParkingSpotDao parkingSpotDao;
    private final ParkingSpotValidator parkingSpotValidator;

    public ParkingSpotServiceImpl(ParkingSpotDao parkingSpotDao, ParkingSpotValidator parkingSpotValidator) {
        this.parkingSpotDao = parkingSpotDao;
        this.parkingSpotValidator = parkingSpotValidator;
    }

    @Override
    public ParkingSpot create(ParkingSpot parkingSpot) {

        if (parkingSpot == null) {
            throw new ValidationException("Parking spot must not be null");
        }

        parkingSpotValidator.validate(parkingSpot);

        if (parkingSpot.getStatus() == ParkingSpotStatus.ARCHIVED) {
            throw new ValidationException(
                    "New parking spot cannot be archived"
            );
        }

        ParkingSpot savedSpot = parkingSpotDao.save(parkingSpot);

        LOGGER.info("Parking spot created successfully. ID={}", savedSpot.getSpotId());

        return savedSpot;
    }

    @Override
    public ParkingSpot findById(Long id) {

        if (id == null || id <= 0) {
            throw new ValidationException("Invalid parking spot id");
        }

        return parkingSpotDao.findById(id).orElseThrow(() -> {
            LOGGER.warn("Parking spot not found. ID={}", id);

            return new ServiceException(
                    "Parking spot not found");
        });
    }

    @Override
    public List<ParkingSpot> findAll(int page, int size) {

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return parkingSpotDao.findAll(page, size);
    }

    @Override
    public ParkingSpot update(ParkingSpot parkingSpot) {

        if (parkingSpot == null) {
            throw new ValidationException("Parking spot must not be null");
        }

        if (parkingSpot.getSpotId() == null) {
            throw new ValidationException("Parking spot id is required");
        }

        parkingSpotValidator.validate(parkingSpot);

        ParkingSpot existingSpot = findById(parkingSpot.getSpotId());

        if (existingSpot.getStatus() == ParkingSpotStatus.ARCHIVED) {
            LOGGER.warn("Attempt to update archived parking spot. ID={}", parkingSpot.getSpotId());
            throw new ValidationException("Archived parking spot cannot be updated");
        }

        ParkingSpot updatedSpot = parkingSpotDao.update(parkingSpot);

        LOGGER.info("Parking spot updated successfully. ID={}", updatedSpot.getSpotId());

        return updatedSpot;
    }

    @Override
    public boolean deleteById(Long id) {

        if (id == null || id <= 0) {
            throw new ValidationException("Invalid parking spot id");
        }

        ParkingSpot parkingSpot = findById(id);

        if (parkingSpot.getStatus() == ParkingSpotStatus.ARCHIVED) {
            LOGGER.warn("Parking spot already archived. ID={}", id);
            return false;
        }

        parkingSpot.setStatus(ParkingSpotStatus.ARCHIVED);

        parkingSpotDao.update(parkingSpot);

        LOGGER.info("Parking spot archived successfully. ID={}", id);

        return true;
    }

    @Override
    public List<ParkingSpot> findByOwnerId(Long ownerId, int page, int size) {

        if (ownerId == null || ownerId <= 0) {
            throw new ValidationException("Invalid owner id");
        }

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return parkingSpotDao.findByOwnerId(ownerId, page, size);
    }

    @Override
    public List<ParkingSpot> findAvailable(int page, int size) {

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return parkingSpotDao.findAvailable(page, size);
    }


}
