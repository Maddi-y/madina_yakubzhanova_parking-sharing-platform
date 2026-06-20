package com.epam.capstone.validation;

import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.Payment;
import com.epam.capstone.model.enums.PaymentMethod;
import com.epam.capstone.model.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentValidatorTest {

    private PaymentValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PaymentValidator();
    }

    private Payment createValidPayment() {

        Booking booking = new Booking();
        booking.setBookingId(1L);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(BigDecimal.valueOf(3000));
        payment.setPaymentMethod(PaymentMethod.CARD);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId("TXN-12345");

        return payment;
    }

    @Test
    void validate_ShouldPass_WhenDataIsValid() {

        Payment payment = createValidPayment();

        assertDoesNotThrow(() -> validator.validate(payment));
    }

    @Test
    void validate_ShouldThrowException_WhenPaymentIsNull() {

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> validator.validate(null)
        );

        assertEquals("Payment must not be null", ex.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenBookingIsNull() {

        Payment payment = createValidPayment();
        payment.setBooking(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(payment)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenBookingIdIsNull() {

        Payment payment = createValidPayment();
        payment.getBooking().setBookingId(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(payment)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenAmountIsNull() {

        Payment payment = createValidPayment();
        payment.setAmount(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(payment)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenAmountIsZero() {

        Payment payment = createValidPayment();
        payment.setAmount(BigDecimal.ZERO);

        assertThrows(ValidationException.class,
                () -> validator.validate(payment)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenPaymentMethodIsNull() {

        Payment payment = createValidPayment();
        payment.setPaymentMethod(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(payment)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenStatusIsNull() {

        Payment payment = createValidPayment();
        payment.setStatus(null);

        assertThrows(ValidationException.class,
                () -> validator.validate(payment)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenTransactionIdIsBlank() {

        Payment payment = createValidPayment();
        payment.setTransactionId("   ");

        assertThrows(ValidationException.class,
                () -> validator.validate(payment)
        );
    }

    @Test
    void validate_ShouldThrowException_WhenTransactionIdTooLong() {

        Payment payment = createValidPayment();
        payment.setTransactionId("a".repeat(300));

        assertThrows(ValidationException.class,
                () -> validator.validate(payment)
        );
    }

    @Test
    void validate_ShouldPass_WhenTransactionIdIsNull() {

        Payment payment = createValidPayment();
        payment.setTransactionId(null);

        assertDoesNotThrow(() -> validator.validate(payment));
    }
}
