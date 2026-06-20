package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Payment;

import java.math.BigDecimal;

public class PaymentValidator implements Validator<Payment> {

    @Override
    public void validate(Payment payment) {

        if (payment == null) {
            throw new ValidationException("Payment must not be null");
        }

        validateBooking(payment);

        validateAmount(payment.getAmount());

        validatePaymentMethod(payment);

        validateStatus(payment);

        validateTransactionId(payment.getTransactionId());
    }

    private void validateBooking(Payment payment) {

        if (payment.getBooking() == null || payment.getBooking().getBookingId() == null) {
            throw new ValidationException("Booking is required");
        }
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null) {
            throw new ValidationException("Amount is required");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be greater than zero");
        }
    }

    private void validatePaymentMethod(Payment payment) {

        if (payment.getPaymentMethod() == null) {
            throw new ValidationException("Payment method is required");
        }
    }

    private void validateStatus(Payment payment) {

        if (payment.getStatus() == null) {
            throw new ValidationException("Payment status is required");
        }
    }

    private void validateTransactionId(String transactionId) {

        if (transactionId == null) {
            return;
        }

        if (transactionId.isBlank()) {
            throw new ValidationException("Transaction id must not be blank");
        }

        if (transactionId.length() > 255) {
            throw new ValidationException("Transaction id must not exceed 255 characters");
        }
    }
}
