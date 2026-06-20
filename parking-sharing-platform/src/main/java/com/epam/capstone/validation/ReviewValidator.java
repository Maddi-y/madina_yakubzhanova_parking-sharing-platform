package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Review;

public class ReviewValidator implements Validator<Review> {

    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;
    private static final int MAX_COMMENT_LENGTH = 1000;

    @Override
    public void validate(Review review) {

        if (review == null) {
            throw new ValidationException("Review must not be null");
        }

        validateBooking(review);
        validateAuthor(review);
        validateRating(review.getRating());
        validateComment(review.getComment());
    }

    private void validateBooking(Review review) {

        if (review.getBooking() == null || review.getBooking().getBookingId() == null) {
            throw new ValidationException("Booking is required");
        }
    }

    private void validateAuthor(Review review) {

        if (review.getAuthor() == null || review.getAuthor().getUserId() == null) {
            throw new ValidationException("Author is required");
        }
    }

    private void validateRating(Integer rating) {

        if (rating == null) {
            throw new ValidationException("Rating is required");
        }

        if (rating < MIN_RATING || rating > MAX_RATING) {
            throw new ValidationException("Rating must be between 1 and 5");
        }
    }

    private void validateComment(String comment) {

        if (comment != null && comment.length() > MAX_COMMENT_LENGTH) {
            throw new ValidationException("Comment must not exceed 1000 characters");
        }
    }
}
