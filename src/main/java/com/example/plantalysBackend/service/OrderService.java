package com.example.plantalysBackend.service;

import com.example.plantalysBackend.model.*;
import com.example.plantalysBackend.repository.OrderRepository;
import com.example.plantalysBackend.repository.PlantRepository;
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
    private PlantRepository plantRepository;
    @Autowired
    private WateringReminderRepository wateringReminderRepo;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    public Order createOrder(Order order) {
        // Vérification du stock pour chaque plante AVANT sauvegarde
        for (OrderItem item : order.getItems()) {
            Plant plant = item.getPlant();
            if (plant == null) {
                throw new IllegalArgumentException("Plante non spécifiée pour un article.");
            }

            int stock = plant.getStock();
            int requested = item.getQuantity();

            if (stock < requested) {
                throw new IllegalStateException("Stock insuffisant pour la plante : " + plant.getName());
            }

            // Mise à jour du stock en mémoire (sera sauvegardé après)
            plant.setStock(stock - requested);
        }

        // Sauvegarde de la commande complète
        Order savedOrder = orderRepository.save(order);

        // Sauvegarde des nouvelles valeurs de stock
        for (OrderItem item : savedOrder.getItems()) {
            plantRepository.save(item.getPlant());
        }

        // Création des rappels d'arrosage
        for (OrderItem item : savedOrder.getItems()) {
            Plant plant = item.getPlant();
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
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

}
