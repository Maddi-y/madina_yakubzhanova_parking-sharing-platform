package com.epam.capstone.service.impl;

import com.epam.capstone.dao.FavouriteDao;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.service.FavouriteService;
import com.epam.capstone.validation.FavouriteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FavouriteServiceImpl implements FavouriteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FavouriteServiceImpl.class);

    private final FavouriteDao favouriteDao;
    private final FavouriteValidator favouriteValidator;

    public FavouriteServiceImpl(FavouriteDao favouriteDao, FavouriteValidator favouriteValidator) {
        this.favouriteDao = favouriteDao;
        this.favouriteValidator = favouriteValidator;
    }

    @Override
    public Favourite add(Favourite favourite) {

        if (favourite == null) {
            throw new ValidationException("Favourite must not be null");
        }

        favouriteValidator.validate(favourite);

        Long userId = favourite.getUser().getUserId();
        Long spotId = favourite.getParkingSpot().getSpotId();

        if (favouriteDao.exists(userId, spotId)) {
            LOGGER.warn("Favourite already exists. User ID={}, Spot ID={}", userId, spotId);

            throw new ValidationException("Parking spot already added to favourites");
        }

        Favourite savedFavourite = favouriteDao.save(favourite);

        LOGGER.info("Favourite added successfully. ID={}", savedFavourite.getFavouriteId());

        return savedFavourite;
    }

    @Override
    public boolean remove(Long favouriteId) {

        if (favouriteId == null || favouriteId <= 0) {
            throw new ValidationException("Invalid favourite id");
        }

        findById(favouriteId);

        boolean deleted = favouriteDao.deleteById(favouriteId);

        LOGGER.info("Favourite removed successfully. ID={}", favouriteId);

        return deleted;
    }

    @Override
    public Favourite findById(Long id) {

        if (id == null || id <= 0) {
            throw new ValidationException("Invalid favourite id");
        }

        return favouriteDao.findById(id).orElseThrow(() -> {
            LOGGER.warn("Favourite not found. ID={}", id);
            return new ServiceException("Favourite not found");
        });
    }

    @Override
    public List<Favourite> findAll(int page, int size) {

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return favouriteDao.findAll(page, size);
    }

    @Override
    public List<Favourite> findByUserId(Long userId, int page, int size) {

        if (userId == null || userId <= 0) {
            throw new ValidationException("Invalid user id");
        }

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return favouriteDao.findByUserId(userId, page, size);
    }

}
