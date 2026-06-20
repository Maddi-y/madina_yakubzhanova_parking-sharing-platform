package com.epam.capstone.service;

import com.epam.capstone.model.Payment;
import com.epam.capstone.model.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {

    Payment create(Payment payment);

    Payment findById(Long id);

    List<Payment> findAll(int page, int size);

    Payment update(Payment payment);

    Payment findByBookingId(Long bookingId);

    Payment findByTransactionId(String transactionId);

    List<Payment> findByStatus(PaymentStatus status, int page, int size);
}
