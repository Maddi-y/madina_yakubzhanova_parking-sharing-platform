package com.epam.capstone.dao;

import com.epam.capstone.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao extends GenericDao<Review, Long> {

    Optional<Review> findByBookingId(Long bookingId);

    List<Review> findByAuthorId(Long authorId, int page, int size);

    List<Review> findByParkingId(Long parkingId, int page, int size);
}
