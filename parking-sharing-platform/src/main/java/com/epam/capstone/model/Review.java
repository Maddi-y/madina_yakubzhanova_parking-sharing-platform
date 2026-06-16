package com.epam.capstone.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Review {

    private Long reviewId;
    private Booking booking;
    private User author;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public Review() {
    }

    public Review(Long reviewId,
                  Booking booking,
                  User author,
                  Integer rating,
                  String comment,
                  LocalDateTime createdAt) {

        this.reviewId = reviewId;
        this.booking = booking;
        this.author = author;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Review review)) {
            return false;
        }

        return Objects.equals(reviewId, review.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId);
    }

    @Override
    public String toString() {

        Long bookingId = null;
        Long authorId = null;

        if (booking != null) {
            bookingId = booking.getBookingId();
        }

        if (author != null) {
            authorId = author.getUserId();
        }

        return "Review{" +
                "reviewId=" + reviewId +
                ", bookingId=" + bookingId +
                ", authorId=" + authorId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
