package com.order.service;

import com.order.common.Payment;
import com.order.common.TransactionRequest;
import com.order.common.TransactionResponse;
import com.order.entity.Order;
import com.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;


    public TransactionResponse save(TransactionRequest request) {
        Order order = request.getOrder();
        Payment payment = request.getPayment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());

        //rest call
        Payment paymentResponse = restTemplate.postForObject("http://PAYMENT-SERVICE/payment/doPayment", payment, Payment.class);
        String message = paymentResponse.getPaymentStatus().equals("Success") ?
                "Payment Processing Successful and Order is Placed "
                : "Payment Processing Failed and Order is Not Placed ";
        orderRepository.save(order);
        return new TransactionResponse(order,
                paymentResponse.getTransactionId(),
                paymentResponse.getAmount(),
                message);
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
