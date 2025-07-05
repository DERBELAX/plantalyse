package com.example.plantalysBackend.service;

import com.example.plantalysBackend.model.*;
import com.example.plantalysBackend.repository.OrderRepository;
import com.example.plantalysBackend.repository.WateringReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WateringReminderRepository wateringReminderRepo;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        // Sauvegarde initiale de la commande
        Order savedOrder = orderRepository.save(order);

        // Génération des rappels d’arrosage pour chaque plante achetée
        for (OrderItem item : savedOrder.getItems()) {
            Plant plant = item.getPlant();
            System.out.println("Traitement item pour plante : " + (plant != null ? plant.getName() : "null"));

            // Vérifie la quantité disponible en stock
            int currentStock = plant.getStock();
            if (currentStock < item.getQuantity()) {
                throw new IllegalStateException("Stock insuffisant pour la plante : " + plant.getName());
            }

            // Diminue le stock
            plant.setStock(currentStock - item.getQuantity());
            
            //Géneration des rappels d'arrosage
            Integer freq = plant.getFrequenceArrosage();

            if (freq == null || freq <= 0) continue;

            int intervalDays = 7 / freq; 

            for (int i = 0; i < freq; i++) {
                WateringReminder reminder = new WateringReminder();
                reminder.setUser(savedOrder.getUser());
                reminder.setPlant(plant);
                reminder.setFrequencyPerWeek(freq);
                reminder.setNextReminder(savedOrder.getCreatedat().plusDays((long) i * intervalDays));

                wateringReminderRepo.save(reminder);
            }
        }

        return savedOrder;
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
