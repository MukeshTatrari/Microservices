package com.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.common.Payment;
import com.order.common.TransactionRequest;
import com.order.common.TransactionResponse;
import com.order.entity.Order;
import com.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RefreshScope
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Value("${microservice.payment-service.endpoints.enpoint.url}")
    private String endPointURL;

    Logger logger= LoggerFactory.getLogger(OrderService.class);
    public TransactionResponse save(TransactionRequest request) throws JsonProcessingException {
        logger.info("Order service Request :::: {} ", new ObjectMapper().writeValueAsString(request));
        Order order = request.getOrder();
        Payment payment = request.getPayment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());

        //rest call
        Payment paymentResponse = restTemplate.postForObject(endPointURL, payment, Payment.class);
        String message = paymentResponse.getPaymentStatus().equals("Success") ?
                "Payment Processing Successful and Order is Placed "
                : "Payment Processing Failed and Order is Not Placed ";
        orderRepository.save(order);
        TransactionResponse response = new TransactionResponse(order,
                paymentResponse.getTransactionId(),
                paymentResponse.getAmount(),
                message);

        logger.info("Order service Response :::: {} " , new ObjectMapper().writeValueAsString(response));
        return response;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order findById(Integer orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public Order updateOrder(Order order) {
        Order order1 = findById(order.getId());
        if (order1 == null) {
            throw new RuntimeException("Order with Id " + order.getId() + " is not present in DB");
        }
        return orderRepository.save(order);
    }

    public String deleteOrder(Order order) {
        orderRepository.delete(order);
        return "Successfully deleted ";
    }
}
