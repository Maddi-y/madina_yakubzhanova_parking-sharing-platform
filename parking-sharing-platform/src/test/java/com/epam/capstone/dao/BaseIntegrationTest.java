package com.epam.capstone.dao;

import com.epam.capstone.dao.impl.*;
import com.epam.capstone.pool.ConnectionPool;

public abstract class BaseIntegrationTest {

    protected static ConnectionPool pool = ConnectionPool.getInstance();

    protected UserDaoImpl userDao = new UserDaoImpl(pool);
    protected ParkingSpotDaoImpl parkingSpotDao = new ParkingSpotDaoImpl(pool);
    protected BookingDaoImpl bookingDao = new BookingDaoImpl(pool);
    protected PaymentDaoImpl paymentDao = new PaymentDaoImpl(pool);
    protected FavouriteDaoImpl favouriteDao = new FavouriteDaoImpl(pool);
    protected ReviewDaoImpl reviewDao = new ReviewDaoImpl(pool);

    /**
     * Универсальный cleanup helper
     * (безопасный, null-safe)
     */
    protected void safeCleanup(
            Long paymentId,
            Long reviewId,
            Long favouriteId,
            Long bookingId,
            Long spotId,
            Long driverId,
            Long ownerId
    ) {
        try {
            if (paymentId != null) {
                paymentDao.deleteById(paymentId);
            }
        } catch (Exception ignored) {}

        try {
            if (reviewId != null) {
                reviewDao.deleteById(reviewId);
            }
        } catch (Exception ignored) {}

        try {
            if (favouriteId != null) {
                favouriteDao.deleteById(favouriteId);
            }
        } catch (Exception ignored) {}

        try {
            if (bookingId != null) {
                bookingDao.deleteById(bookingId);
            }
        } catch (Exception ignored) {}

        try {
            if (spotId != null) {
                parkingSpotDao.deleteById(spotId);
            }
        } catch (Exception ignored) {}

        try {
            if (driverId != null) {
                userDao.deleteById(driverId);
            }
        } catch (Exception ignored) {}

        try {
            if (ownerId != null) {
                userDao.deleteById(ownerId);
            }
        } catch (Exception ignored) {}
    }
}