package com.epam.capstone.service;

import com.epam.capstone.model.Review;

import java.util.List;

public interface ReviewService {

    Review create(Review review);

    Review findById(Long id);

    List<Review> findAll(int page, int size);

    Review update(Review review);

    boolean deleteById(Long id);

    Review findByBookingId(Long bookingId);

    List<Review> findByAuthorId(Long authorId, int page, int size);
}
