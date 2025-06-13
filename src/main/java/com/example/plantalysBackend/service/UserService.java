package com.example.plantalysBackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.plantalysBackend.model.User;
import com.example.plantalysBackend.repository.UserRepository;
import com.example.plantalysBackend.dto.UserDTO;

@Service
public class UserService {
@Autowired
private UserRepository userRepository;
@Autowired
private BCryptPasswordEncoder passwordEncoder;

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
    user.setPassword(dto.getPassword());
    user.setRoles(dto.getRoles());

    return userRepository.save(user);
}

//delete user

public void deleteUser(Long id) {
	if (!userRepository.existsById(id)) {
        throw new RuntimeException("Utilisateur non trouvé");
    }
    userRepository.deleteById(id);
}




}
