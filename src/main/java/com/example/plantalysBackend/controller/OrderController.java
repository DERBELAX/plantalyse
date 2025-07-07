package com.example.plantalysBackend.controller;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.plantalysBackend.dto.OrderDTO;
import com.example.plantalysBackend.dto.OrderItemDTO;
import com.example.plantalysBackend.dto.OrderResponseDTO;
import com.example.plantalysBackend.model.Order;
import com.example.plantalysBackend.model.OrderItem;
import com.example.plantalysBackend.model.Plant;
import com.example.plantalysBackend.model.User;
import com.example.plantalysBackend.repository.PlantRepository;
import com.example.plantalysBackend.repository.UserRepository;
import com.example.plantalysBackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlantRepository plantRepository;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderService.getOrderById(id);
        return optionalOrder.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Commande non trouvée"));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        try {
            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok("Commande supprimée avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/from-cart")
    public ResponseEntity<?> createOrderFromCart(@RequestBody List<OrderItemDTO> cartItems, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non authentifié.");
        }

        if (cartItems == null || cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("Le panier est vide.");
        }

        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur introuvable.");
        }

        Order order = new Order();
        order.setStatus("EN_ATTENTE");
        order.setCreatedat(LocalDateTime.now());
        order.setUser(user);

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemDTO itemDTO : cartItems) {
            if (itemDTO.getId() == null) {
                return ResponseEntity.badRequest().body("ID de plante manquant.");
            }

            if (itemDTO.getQuantity() <= 0 || itemDTO.getUnite_price() <= 0) {
                return ResponseEntity.badRequest()
                        .body("Quantité ou prix invalide pour l'article ID : " + itemDTO.getId());
            }

            Optional<Plant> plantOpt = plantRepository.findById(itemDTO.getId());
            if (plantOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Plante introuvable pour l'ID : " + itemDTO.getId());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setPlant(plantOpt.get());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnite_price(itemDTO.getUnite_price());

            items.add(orderItem);
        }

        order.setItems(items);

        try {
            // Appel du service qui vérifie le stock, diminue, sauvegarde, crée rappels
            Order savedOrder = orderService.createOrder(order);

            // Construction de la réponse DTO
            OrderResponseDTO responseDTO = new OrderResponseDTO();
            responseDTO.setOrderId(savedOrder.getId());
            responseDTO.setStatus(savedOrder.getStatus());
            responseDTO.setCreatedAt(savedOrder.getCreatedat());

            List<OrderItemDTO> responseItems = new ArrayList<>();
            for (OrderItem item : savedOrder.getItems()) {
                OrderItemDTO dto = new OrderItemDTO();
                dto.setId(item.getPlant().getId());
                dto.setPlantName(item.getPlant().getName());
                dto.setQuantity(item.getQuantity());
                dto.setUnite_price(item.getUnite_price());
                responseItems.add(dto);
            }

            responseDTO.setItems(responseItems);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

        } catch (IllegalStateException e) {
            // Cas de stock insuffisant
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la commande.");
        }
    }

    
    @GetMapping("/mine")
    public ResponseEntity<?> getMyOrders(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non authentifié.");
        }

        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur introuvable.");
        }

        List<Order> userOrders = orderService.getOrdersByUser(user);
        List<OrderDTO> dtos = userOrders.stream()
            .map(OrderDTO::new)
            .toList();

        return ResponseEntity.ok(dtos);
    }


}
