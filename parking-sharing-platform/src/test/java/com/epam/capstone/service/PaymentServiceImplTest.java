package com.epam.capstone.service;

import com.epam.capstone.dao.PaymentDao;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Booking;
import com.epam.capstone.model.Payment;
import com.epam.capstone.model.enums.PaymentMethod;
import com.epam.capstone.model.enums.PaymentStatus;
import com.epam.capstone.service.impl.PaymentServiceImpl;
import com.epam.capstone.validation.PaymentValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentDao paymentDao;

    @Mock
    private PaymentValidator paymentValidator;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment buildPayment() {

        Booking booking = new Booking();
        booking.setBookingId(1L);

        Payment payment = new Payment();

        payment.setPaymentId(1L);
        payment.setBooking(booking);
        payment.setAmount(BigDecimal.valueOf(5000));
        payment.setPaymentMethod(PaymentMethod.CARD);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId("TXN-123");

        return payment;
    }

    @Test
    void create_ShouldSavePayment_WhenDataIsValid() {

        Payment payment = buildPayment();

        when(paymentDao.findByBookingId(1L)).thenReturn(Optional.empty());
        when(paymentDao.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.create(payment);

        assertNotNull(result);

        verify(paymentValidator).validate(payment);
        verify(paymentDao).save(payment);
    }

    @Test
    void create_ShouldThrowException_WhenPaymentIsNull() {

        assertThrows(ValidationException.class, () -> paymentService.create(null));

        verifyNoInteractions(paymentDao);
    }

    @Test
    void create_ShouldThrowException_WhenPaymentAlreadyExistsForBooking() {

        Payment payment = buildPayment();

        when(paymentDao.findByBookingId(1L)).thenReturn(Optional.of(payment));

        assertThrows(ValidationException.class, () -> paymentService.create(payment));

        verify(paymentValidator).validate(payment);
        verify(paymentDao, never()).save(any());
    }

    @Test
    void findById_ShouldReturnPayment_WhenPaymentExists() {

        Payment payment = buildPayment();

        when(paymentDao.findById(1L)).thenReturn(Optional.of(payment));

        Payment result = paymentService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getPaymentId());

        verify(paymentDao).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenIdInvalid() {

        assertThrows(ValidationException.class, () -> paymentService.findById(0L));

        verifyNoInteractions(paymentDao);
    }

    @Test
    void findById_ShouldThrowException_WhenPaymentNotFound() {

        when(paymentDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> paymentService.findById(1L));

        verify(paymentDao).findById(1L);
    }

    @Test
    void findAll_ShouldReturnPayments() {

        List<Payment> payments = List.of(buildPayment(), buildPayment());

        when(paymentDao.findAll(1, 10)).thenReturn(payments);

        List<Payment> result = paymentService.findAll(1, 10);

        assertEquals(2, result.size());

        verify(paymentDao).findAll(1, 10);
    }

    @Test
    void findAll_ShouldThrowException_WhenPageInvalid() {

        assertThrows(ValidationException.class, () -> paymentService.findAll(0, 10));

        verifyNoInteractions(paymentDao);
    }

    @Test
    void findAll_ShouldThrowException_WhenSizeInvalid() {

        assertThrows(ValidationException.class, () -> paymentService.findAll(1, 0));

        verifyNoInteractions(paymentDao);
    }

    @Test
    void update_ShouldUpdatePayment_WhenDataIsValid() {

        Payment payment = buildPayment();

        Payment existingPayment = buildPayment();
        existingPayment.setStatus(PaymentStatus.PENDING);

        when(paymentDao.findById(1L)).thenReturn(Optional.of(existingPayment));
        when(paymentDao.update(payment)).thenReturn(payment);

        Payment result = paymentService.update(payment);

        assertNotNull(result);

        verify(paymentValidator).validate(payment);
        verify(paymentDao).update(payment);
    }

    @Test
    void update_ShouldThrowException_WhenPaymentIsNull() {

        assertThrows(ValidationException.class, () -> paymentService.update(null));

        verifyNoInteractions(paymentDao);
    }

    @Test
    void update_ShouldThrowException_WhenPaymentIdIsNull() {

        Payment payment = buildPayment();
        payment.setPaymentId(null);

        assertThrows(ValidationException.class, () -> paymentService.update(payment));

        verify(paymentValidator, never()).validate(any());
        verifyNoInteractions(paymentDao);
    }

    @Test
    void update_ShouldThrowException_WhenPaymentAlreadyPaid() {

        Payment payment = buildPayment();

        Payment existingPayment = buildPayment();
        existingPayment.setStatus(PaymentStatus.PAID);

        when(paymentDao.findById(1L)).thenReturn(Optional.of(existingPayment));

        assertThrows(ValidationException.class, () -> paymentService.update(payment));

        verify(paymentValidator).validate(payment);
        verify(paymentDao, never()).update(any());
    }

    @Test
    void update_ShouldThrowException_WhenPaymentAlreadyRefunded() {

        Payment payment = buildPayment();

        Payment existingPayment = buildPayment();
        existingPayment.setStatus(PaymentStatus.REFUNDED);

        when(paymentDao.findById(1L)).thenReturn(Optional.of(existingPayment));

        assertThrows(ValidationException.class, () -> paymentService.update(payment));

        verify(paymentValidator).validate(payment);
        verify(paymentDao, never()).update(any());
    }

    @Test
    void findByBookingId_ShouldReturnPayment() {

        Payment payment = buildPayment();

        when(paymentDao.findByBookingId(1L)).thenReturn(Optional.of(payment));

        Payment result = paymentService.findByBookingId(1L);

        assertNotNull(result);
        assertEquals(payment.getPaymentId(), result.getPaymentId());

        verify(paymentDao).findByBookingId(1L);
    }

    @Test
    void findByBookingId_ShouldThrowException_WhenBookingIdInvalid() {

        assertThrows(ValidationException.class, () -> paymentService.findByBookingId(0L));

        verifyNoInteractions(paymentDao);
    }

    @Test
    void findByTransactionId_ShouldReturnPayment() {

        Payment payment = buildPayment();

        when(paymentDao.findByTransactionId("TXN-123")).thenReturn(Optional.of(payment));

        Payment result = paymentService.findByTransactionId("TXN-123");

        assertNotNull(result);
        assertEquals(payment.getPaymentId(), result.getPaymentId());

        verify(paymentDao).findByTransactionId("TXN-123");
    }

    @Test
    void findByTransactionId_ShouldThrowException_WhenTransactionIdBlank() {

        assertThrows(ValidationException.class, () -> paymentService.findByTransactionId(" "));

        verifyNoInteractions(paymentDao);
    }

    @Test
    void findByStatus_ShouldReturnPayments() {

        List<Payment> payments = List.of(buildPayment(), buildPayment());

        when(paymentDao.findByStatus(PaymentStatus.PENDING, 1, 10)).thenReturn(payments);

        List<Payment> result = paymentService.findByStatus(PaymentStatus.PENDING, 1, 10);

        assertEquals(2, result.size());

        verify(paymentDao).findByStatus(PaymentStatus.PENDING, 1, 10);
    }

    @Test
    void findByStatus_ShouldThrowException_WhenStatusIsNull() {

        assertThrows(ValidationException.class, () -> paymentService.findByStatus(null, 1, 10));

        verifyNoInteractions(paymentDao);
    }
}
