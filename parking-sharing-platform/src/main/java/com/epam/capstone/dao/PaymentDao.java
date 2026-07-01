package com.epam.capstone.dao;

import com.epam.capstone.model.Payment;
import com.epam.capstone.model.enums.PaymentStatus;

import java.util.List;
import java.util.Optional;

public interface PaymentDao extends GenericDao<Payment, Long> {

    Optional<Payment> findByBookingId(Long bookingId);

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByStatus(PaymentStatus status, int page, int size);

    void updateStatus(Long paymentId, PaymentStatus status);
}
