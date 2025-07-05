package com.example.plantalysBackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.plantalysBackend.model.Blog;
import com.example.plantalysBackend.model.CommentCommunity;
import com.example.plantalysBackend.model.LikeCommunity;
import com.example.plantalysBackend.model.Order;
import com.example.plantalysBackend.model.PostCommunity;
import com.example.plantalysBackend.model.Review;
import com.example.plantalysBackend.model.User;
import com.example.plantalysBackend.model.WateringReminder;
import com.example.plantalysBackend.repository.BlogRepository;
import com.example.plantalysBackend.repository.CommentRepository;
import com.example.plantalysBackend.repository.LikeCommunityRepository;
import com.example.plantalysBackend.repository.OrderRepository;
import com.example.plantalysBackend.repository.PostCommunityRepository;
import com.example.plantalysBackend.repository.ReviewRepository;
import com.example.plantalysBackend.repository.UserRepository;
import com.example.plantalysBackend.repository.WateringReminderRepository;

import jakarta.transaction.Transactional;

import com.example.plantalysBackend.dto.UserDTO;


@Service
public class UserService {
@Autowired
private UserRepository userRepository;
@Autowired
private BCryptPasswordEncoder passwordEncoder;
@Autowired
private OrderRepository orderRepository;

@Autowired
private PostCommunityRepository postCommunityRepository;
@Autowired
private LikeCommunityRepository likeCommunityRepository;
@Autowired
private CommentRepository commentRepository;
@Autowired
private BlogRepository blogRepository;
@Autowired
private ReviewRepository reviewRepository;
@Autowired
private WateringReminderRepository wateringReminderRepository;

//create user
public User createUser(UserDTO dto) {
    User user = new User();
    user.setFirstname(dto.getFirstname());
    user.setLastname(dto.getLastname());
    user.setEmail(dto.getEmail());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setRoles(dto.getRoles());

    return userRepository.save(user);
}

//get All users

public List<User> getAllUsers() {
	    return userRepository.findAll();
	}

//get user by id
public User getUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
}

//upusers 

public User updateUser(Long id, UserDTO dto) {
    User user = getUserById(id);
    user.setFirstname(dto.getFirstname());
    user.setLastname(dto.getLastname());
    user.setEmail(dto.getEmail());
    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
    }
    user.setRoles(dto.getRoles());

    return userRepository.save(user);
}

//delete user
@Transactional
public void deleteUser(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    //  Détacher les commandes
    List<Order> orders = orderRepository.findByUser(user);
    for (Order order : orders) {
        order.setUser(null);
    }
    orderRepository.saveAll(orders);

    // Détacher les posts communautaires
    List<PostCommunity> posts = postCommunityRepository.findByUser(user);
    for (PostCommunity post : posts) {
        post.setUser(null);
    }
    postCommunityRepository.saveAll(posts);

    // Détacher les blogs
    List<Blog> blogs = blogRepository.findByUser(user);
    for (Blog blog : blogs) {
        blog.setUser(null);
    }
    blogRepository.saveAll(blogs);

    // Supprimer les commentaires (si tu veux les conserver, fais comme pour les autres : setUser(null))
    List<CommentCommunity> comments = commentRepository.findByUser(user);
    commentRepository.deleteAll(comments);

    // Supprimer les likes (même logique que les commentaires)
    List<LikeCommunity> likes = likeCommunityRepository.findByUser(user);
    likeCommunityRepository.deleteAll(likes);
    
    
    //supprimer les review
    List<Review> reviews = reviewRepository.findByUser(user);
    for (Review review : reviews) {
        review.setUser(null);
    }
    reviewRepository.saveAll(reviews);
 // Détacher les rappels d’arrosage
    List<WateringReminder> reminders = wateringReminderRepository.findByUser(user);
    for (WateringReminder reminder : reminders) {
        reminder.setUser(null);
    }
    wateringReminderRepository.saveAll(reminders);

    // supprimer l'utilisateur
    userRepository.delete(user);
}


}
