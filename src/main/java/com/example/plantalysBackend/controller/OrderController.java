package com.example.plantalysBackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.plantalysBackend.model.Order;
import com.example.plantalysBackend.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
	 @Autowired
	    private OrderService orderService;

	    @GetMapping
	    public List<Order> getAllOrders() {
	        return orderService.getAllOrders();
	    }
	    @GetMapping("/{id}")
	    public Optional<Order> getOrderById(@PathVariable Long id) {
	        return orderService.getOrderById(id);
	    }

	    @PostMapping
	    public Order createOrder(@RequestBody Order order) {
	        return orderService.createOrder(order);
	    }

	    @PutMapping("/{id}")
	    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
	        return orderService.updateOrder(id, order);
	    }

	    @DeleteMapping("/{id}")
	    public void deleteOrder(@PathVariable Long id) {
	        orderService.deleteOrder(id);
	    }

}
