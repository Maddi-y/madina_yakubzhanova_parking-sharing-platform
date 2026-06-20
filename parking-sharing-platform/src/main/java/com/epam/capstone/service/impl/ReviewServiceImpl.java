package com.epam.capstone.service.impl;

import com.epam.capstone.dao.ReviewDao;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.Review;
import com.epam.capstone.model.enums.BookingStatus;
import com.epam.capstone.service.ReviewService;
import com.epam.capstone.validation.ReviewValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewDao reviewDao;
    private final ReviewValidator reviewValidator;

    public ReviewServiceImpl(ReviewDao reviewDao, ReviewValidator reviewValidator) {

        this.reviewDao = reviewDao;
        this.reviewValidator = reviewValidator;
    }

    @Override
    public Review create(Review review) {

        if (review == null) {
            throw new ValidationException("Review must not be null");
        }

        reviewValidator.validate(review);

        Long bookingId = review.getBooking().getBookingId();

        if (reviewDao.findByBookingId(bookingId).isPresent()) {
            LOGGER.warn("Review already exists for booking. Booking ID={}", bookingId);

            throw new ValidationException("Review for this booking already exists");
        }

        Booking booking = review.getBooking();

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new ValidationException("Review can only be left for completed booking");
        }

        Review savedReview = reviewDao.save(review);

        LOGGER.info("Review created successfully. ID={}", savedReview.getReviewId());

        return savedReview;
    }

    @Override
    public Review findById(Long id) {

        if (id == null || id <= 0) {
            throw new ValidationException("Invalid review id");
        }

        return reviewDao.findById(id).orElseThrow(() -> {
            LOGGER.warn("Review not found. ID={}", id);

            return new ServiceException("Review not found");
        });
    }

    @Override
    public List<Review> findAll(int page, int size) {

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return reviewDao.findAll(page, size);
    }

    @Override
    public Review update(Review review) {

        if (review == null) {
            throw new ValidationException("Review must not be null");
        }

        if (review.getReviewId() == null) {
            throw new ValidationException("Review id is required");
        }

        reviewValidator.validate(review);

        findById(review.getReviewId());

        Review updatedReview = reviewDao.update(review);

        LOGGER.info("Review updated successfully. ID={}", updatedReview.getReviewId());

        return updatedReview;
    }

    @Override
    public boolean deleteById(Long id) {

        if (id == null || id <= 0) {
            throw new ValidationException("Invalid review id");
        }

        findById(id);

        boolean deleted = reviewDao.deleteById(id);

        LOGGER.info("Review deleted successfully. ID={}", id);

        return deleted;
    }

    @Override
    public Review findByBookingId(Long bookingId) {

        if (bookingId == null || bookingId <= 0) {
            throw new ValidationException("Invalid booking id");
        }

        return reviewDao.findByBookingId(bookingId).orElseThrow(() -> {
            LOGGER.warn("Review not found for booking. Booking ID={}", bookingId);

            return new ServiceException("Review not found");
        });
    }

    @Override
    public List<Review> findByAuthorId(Long authorId, int page, int size) {

        if (authorId == null || authorId <= 0) {
            throw new ValidationException("Invalid author id");
        }

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return reviewDao.findByAuthorId(authorId, page, size);
    }
}
