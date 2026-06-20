package com.epam.capstone.service.impl;

import com.epam.capstone.dao.PaymentDao;
import com.epam.capstone.exception.ServiceException;
import com.epam.capstone.exception.ValidationException;
import com.epam.capstone.model.Payment;
import com.epam.capstone.model.enums.PaymentStatus;
import com.epam.capstone.service.PaymentService;
import com.epam.capstone.validation.PaymentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentDao paymentDao;
    private final PaymentValidator paymentValidator;

    public PaymentServiceImpl(PaymentDao paymentDao, PaymentValidator paymentValidator) {
        this.paymentDao = paymentDao;
        this.paymentValidator = paymentValidator;
    }

    @Override
    public Payment create(Payment payment) {

        if (payment == null) {
            throw new ValidationException("Payment must not be null");
        }

        paymentValidator.validate(payment);

        Long bookingId = payment.getBooking().getBookingId();

        if (paymentDao.findByBookingId(bookingId).isPresent()) {
            LOGGER.warn(
                    "Payment already exists for booking. Booking ID={}", bookingId);

            throw new ValidationException("Payment for this booking already exists");
        }

        Payment savedPayment = paymentDao.save(payment);

        LOGGER.info("Payment created successfully. ID={}", savedPayment.getPaymentId());

        return savedPayment;
    }

    @Override
    public Payment findById(Long id) {

        if (id == null || id <= 0) {
            throw new ValidationException("Invalid payment id");
        }

        return paymentDao.findById(id).orElseThrow(() -> {
            LOGGER.warn("Payment not found. ID={}", id);

            return new ServiceException("Payment not found");
        });
    }

    @Override
    public List<Payment> findAll(int page, int size) {

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return paymentDao.findAll(page, size);
    }

    @Override
    public Payment update(Payment payment) {

        if (payment == null) {
            throw new ValidationException("Payment must not be null");
        }

        if (payment.getPaymentId() == null) {
            throw new ValidationException("Payment id is required");
        }

        paymentValidator.validate(payment);

        Payment existingPayment = findById(payment.getPaymentId());

        if (existingPayment.getStatus() == PaymentStatus.PAID) {

            LOGGER.warn("Attempt to update paid payment. ID={}", payment.getPaymentId());

            throw new ValidationException("Paid payment cannot be updated");
        }

        Payment updatedPayment = paymentDao.update(payment);

        LOGGER.info("Payment updated successfully. ID={}", updatedPayment.getPaymentId());

        return updatedPayment;
    }

    @Override
    public Payment findByBookingId(Long bookingId) {

        if (bookingId == null || bookingId <= 0) {
            throw new ValidationException("Invalid booking id");
        }

        return paymentDao.findByBookingId(bookingId).orElseThrow(() -> {
            LOGGER.warn("Payment not found. Booking ID={}", bookingId);
            return new ServiceException("Payment not found");
        });
    }

    @Override
    public Payment findByTransactionId(String transactionId) {

        if (transactionId == null || transactionId.isBlank()) {
            throw new ValidationException("Transaction id is required");
        }

        return paymentDao.findByTransactionId(transactionId).orElseThrow(() -> {

            LOGGER.warn("Payment not found. Transaction ID={}", transactionId);
            return new ServiceException("Payment not found");

        });
    }

    @Override
    public List<Payment> findByStatus(PaymentStatus status, int page, int size) {

        if (status == null) {
            throw new ValidationException("Payment status is required");
        }

        if (page <= 0) {
            throw new ValidationException("Page must be greater than zero");
        }

        if (size <= 0) {
            throw new ValidationException("Size must be greater than zero");
        }

        return paymentDao.findByStatus(status, page, size);
    }

}