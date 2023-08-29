package kz.askar.shop.service;


import kz.askar.shop.entity.*;
import kz.askar.shop.repository.OrderRepository;
import kz.askar.shop.repository.OrderedProductRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class OrderService {


    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository, OrderedProductRepository orderedProductRepository) {
        this.orderRepository = orderRepository;
    }


    public Order createOrder(User user, String address) {

        Order order = new Order();

        order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
        order.setStatus(Status.PROCESSING);
        order.setUser(user);
        order.setAddress(address);


        orderRepository.save(order);


        return order;
    }
}
