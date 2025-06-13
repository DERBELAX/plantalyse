package com.example.plantalysBackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.plantalysBackend.model.Order;
import com.example.plantalysBackend.repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
    private OrderRepository orderRepository;
	public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(updatedOrder.getStatus());
            order.setCreatedat(updatedOrder.getCreatedat());
            return orderRepository.save(order);
        }).orElseGet(() -> {
            updatedOrder.setId_order(id);
            return orderRepository.save(updatedOrder);
        });
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

}
