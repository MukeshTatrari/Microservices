package com.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.entity.Payment;
import com.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    Logger logger= LoggerFactory.getLogger(PaymentService.class);

    public Payment savePayment(Payment payment) throws JsonProcessingException {
        logger.info("Payment Request  ::: {} ", new ObjectMapper().writeValueAsString(payment));
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentStatus(getRandomPaymentStatus());
        Payment response =  paymentRepository.save(payment);
        logger.info("Payment response  ::: {} ", new ObjectMapper().writeValueAsString(response));
        return response;
    }

    public Payment findPaymentHistoryByOrderId(Integer orderId) throws JsonProcessingException {
        logger.info("OrderId   ::: {} ", orderId);
        Payment response =  paymentRepository.findByOrderId(orderId);
        logger.info("Payment response  ::: {} ", new ObjectMapper().writeValueAsString(response));
        return response;
    }

    private String getRandomPaymentStatus() {
        //api should be 3rd Party payment Gateway
        return new Random().nextBoolean() ? "Success" : "Failed";
    }
}
