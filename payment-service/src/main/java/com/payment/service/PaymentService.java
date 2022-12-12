package com.payment.service;

import com.payment.entity.Payment;
import com.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment) {
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentStatus(getRandomPaymentStatus());
        return paymentRepository.save(payment);
    }

    public Payment findPaymentHistoryByOrderId(Integer orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    private String getRandomPaymentStatus() {
        //api should be 3rd Party payment Gateway
        return new Random().nextBoolean() ? "Success" : "Failed";
    }
}
