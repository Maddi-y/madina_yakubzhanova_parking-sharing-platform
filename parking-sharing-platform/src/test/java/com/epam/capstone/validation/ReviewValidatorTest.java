package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.Review;
import com.epam.capstone.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewValidatorTest {

    private ReviewValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ReviewValidator();
    }

    private Review createValidReview() {

        Booking booking = new Booking();
        booking.setBookingId(1L);

        User author = new User();
        author.setUserId(1L);

        Review review = new Review();
        review.setBooking(booking);
        review.setAuthor(author);
        review.setRating(5);
        review.setComment("Good service");

        return review;
    }

    @Test
    void validate_ShouldPass_WhenDataIsValid() {

        Review review = createValidReview();

        assertDoesNotThrow(() -> validator.validate(review));
    }

    @Test
    void validate_ShouldThrowException_WhenReviewIsNull() {

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(null)
        );

        assertEquals("Review must not be null", ex.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenBookingIsNull() {

        Review review = createValidReview();
        review.setBooking(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(review)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenBookingIdIsNull() {

        Review review = createValidReview();
        review.getBooking().setBookingId(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(review)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenAuthorIsNull() {

        Review review = createValidReview();
        review.setAuthor(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(review)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenAuthorIdIsNull() {

        Review review = createValidReview();
        review.getAuthor().setUserId(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(review)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenRatingIsNull() {

        Review review = createValidReview();
        review.setRating(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(review)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenRatingTooLow() {

        Review review = createValidReview();
        review.setRating(0);

        assertThrows(ValidationException.class,
                () -> validator.validate(review)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenRatingTooHigh() {

        Review review = createValidReview();
        review.setRating(6);

        assertThrows(ValidationException.class,
                () -> validator.validate(review)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenCommentTooLong() {

        Review review = createValidReview();
        review.setComment("a".repeat(1001));

        assertThrows(ValidationException.class,
                () -> validator.validate(review)
        );
    }

    @Test
    void validate_ShouldPass_WhenCommentIsNull() {

        Review review = createValidReview();
        review.setComment(null);

        assertDoesNotThrow(() -> validator.validate(review));
    }
}