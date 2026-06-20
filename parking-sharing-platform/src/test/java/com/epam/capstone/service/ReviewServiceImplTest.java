package com.epam.capstone.service;

import com.epam.capstone.dao.ReviewDao;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.Review;
import com.epam.capstone.model.User;
import com.epam.capstone.validation.ReviewValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewDao reviewDao;

    @Mock
    private ReviewValidator reviewValidator;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review buildReview() {

        User author = new User();
        author.setUserId(1L);

        Booking booking = new Booking();
        booking.setBookingId(10L);

        Review review = new Review();

        review.setReviewId(100L);
        review.setAuthor(author);
        review.setBooking(booking);
        review.setRating(5);
        review.setComment("Excellent parking spot");

        return review;
    }

    @Test
    void create_ShouldSaveReview() {

        Review review = buildReview();

        when(reviewDao.findByBookingId(10L)).thenReturn(Optional.empty());
        when(reviewDao.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Review result = reviewService.create(review);

        assertNotNull(result);
        assertEquals(review.getReviewId(), result.getReviewId());

        verify(reviewValidator).validate(review);
        verify(reviewDao).save(review);
    }

    @Test
    void create_ShouldThrowException_WhenReviewIsNull() {

        assertThrows(ValidationException.class, () -> reviewService.create(null));
    }

    @Test
    void create_ShouldThrowException_WhenReviewAlreadyExistsForBooking() {

        Review review = buildReview();

        when(reviewDao.findByBookingId(10L)).thenReturn(Optional.of(review));

        assertThrows(ValidationException.class, () -> reviewService.create(review));

        verify(reviewDao, never()).save(any());
    }

    @Test
    void findById_ShouldReturnReview() {

        Review review = buildReview();

        when(reviewDao.findById(100L)).thenReturn(Optional.of(review));

        Review result = reviewService.findById(100L);

        assertNotNull(result);
        assertEquals(review.getReviewId(), result.getReviewId());

        verify(reviewDao).findById(100L);
    }

    @Test
    void findById_ShouldThrowException_WhenReviewNotFound() {

        when(reviewDao.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> reviewService.findById(100L));
    }

    @Test
    void findById_ShouldThrowException_WhenIdInvalid() {

        assertThrows(ValidationException.class, () -> reviewService.findById(0L));

        verify(reviewDao, never()).findById(anyLong());
    }

    @Test
    void findAll_ShouldReturnReviews() {

        Review review = buildReview();

        when(reviewDao.findAll(1, 10)).thenReturn(List.of(review));

        List<Review> result = reviewService.findAll(1, 10);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(reviewDao).findAll(1, 10);
    }

    @Test
    void findAll_ShouldThrowException_WhenPageInvalid() {

        assertThrows(ValidationException.class, () -> reviewService.findAll(0, 10));

        verify(reviewDao, never()).findAll(anyInt(), anyInt());
    }

    @Test
    void findAll_ShouldThrowException_WhenSizeInvalid() {

        assertThrows(ValidationException.class, () -> reviewService.findAll(1, 0));

        verify(reviewDao, never()).findAll(anyInt(), anyInt());
    }

    @Test
    void update_ShouldUpdateReview() {

        Review review = buildReview();

        when(reviewDao.findById(100L)).thenReturn(Optional.of(review));
        when(reviewDao.update(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Review result = reviewService.update(review);

        assertNotNull(result);
        assertEquals(review.getReviewId(), result.getReviewId());

        verify(reviewValidator).validate(review);
        verify(reviewDao).update(review);
    }

    @Test
    void update_ShouldThrowException_WhenReviewIsNull() {

        assertThrows(ValidationException.class, () -> reviewService.update(null));

        verify(reviewDao, never()).update(any());
    }

    @Test
    void update_ShouldThrowException_WhenReviewIdIsNull() {

        Review review = buildReview();
        review.setReviewId(null);

        assertThrows(ValidationException.class, () -> reviewService.update(review));

        verify(reviewDao, never()).update(any());
    }

    @Test
    void deleteById_ShouldDeleteReview() {

        Review review = buildReview();

        when(reviewDao.findById(100L)).thenReturn(Optional.of(review));
        when(reviewDao.deleteById(100L)).thenReturn(true);

        boolean result = reviewService.deleteById(100L);

        assertTrue(result);

        verify(reviewDao).deleteById(100L);
    }

    @Test
    void deleteById_ShouldThrowException_WhenIdInvalid() {

        assertThrows(ValidationException.class, () -> reviewService.deleteById(0L));

        verify(reviewDao, never()).deleteById(anyLong());
    }

    @Test
    void findByBookingId_ShouldReturnReview() {

        Review review = buildReview();

        when(reviewDao.findByBookingId(10L)).thenReturn(Optional.of(review));

        Review result = reviewService.findByBookingId(10L);

        assertNotNull(result);
        assertEquals(review.getReviewId(), result.getReviewId());

        verify(reviewDao).findByBookingId(10L);
    }

    @Test
    void findByBookingId_ShouldThrowException_WhenBookingIdInvalid() {

        assertThrows(ValidationException.class, () -> reviewService.findByBookingId(0L));

        verify(reviewDao, never()).findByBookingId(anyLong());
    }

    @Test
    void findByBookingId_ShouldThrowException_WhenReviewNotFound() {

        when(reviewDao.findByBookingId(10L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> reviewService.findByBookingId(10L));
    }

    @Test
    void findByAuthorId_ShouldReturnReviews() {

        Review review = buildReview();

        when(reviewDao.findByAuthorId(1L, 1, 10)).thenReturn(List.of(review));

        List<Review> result = reviewService.findByAuthorId(1L, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(reviewDao).findByAuthorId(1L, 1, 10);
    }

    @Test
    void findByAuthorId_ShouldThrowException_WhenAuthorIdInvalid() {

        assertThrows(ValidationException.class, () -> reviewService.findByAuthorId(0L, 1, 10));

        verify(reviewDao, never()).findByAuthorId(anyLong(), anyInt(), anyInt());
    }

    @Test
    void findByAuthorId_ShouldThrowException_WhenPageInvalid() {

        assertThrows(ValidationException.class, () -> reviewService.findByAuthorId(1L, 0, 10));

        verify(reviewDao, never()).findByAuthorId(anyLong(), anyInt(), anyInt());
    }

    @Test
    void findByAuthorId_ShouldThrowException_WhenSizeInvalid() {

        assertThrows(ValidationException.class, () -> reviewService.findByAuthorId(1L, 1, 0));

        verify(reviewDao, never()).findByAuthorId(anyLong(), anyInt(), anyInt());
    }


}