package com.example.plantalysBackend.controller;

import com.example.plantalysBackend.model.Blog;
import com.example.plantalysBackend.model.Plant;
import com.example.plantalysBackend.model.User;
import com.example.plantalysBackend.repository.BlogRepository;
import com.example.plantalysBackend.repository.PlantRepository;
import com.example.plantalysBackend.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/blogs")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminBlogController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public ResponseEntity<?> getAllBlogs() {
        return ResponseEntity.ok(blogRepository.findAll());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createBlog(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "plantIds", required = false) List<Long> plantIds,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Principal principal) {

        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Le titre est requis");
        }

        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setCreatedat(LocalDateTime.now());
        blog.setUpdatedat(LocalDateTime.now());

        // Lier l'utilisateur
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }
        blog.setUser(user);

        // Upload de l’image
        if (image != null && !image.isEmpty()) {
            try {
                String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filepath = Paths.get(uploadPath, filename);
                Files.copy(image.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
                blog.setImage("/uploads/" + filename);
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Erreur lors de l’upload : " + e.getMessage());
            }
        }

        // Lier les plantes
        if (plantIds != null && !plantIds.isEmpty()) {
            List<Plant> plants = plantRepository.findAllById(plantIds);
            blog.setPlants(plants);
        }

        blogRepository.save(blog);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateBlog(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "plantIds", required = false) List<Long> plantIds,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog == null) {
            return ResponseEntity.notFound().build();
        }

        blog.setTitle(title);
        blog.setDescription(description);
        blog.setUpdatedat(LocalDateTime.now());

        // Mise à jour des plantes
        if (plantIds != null) {
            List<Plant> plants = plantRepository.findAllById(plantIds);
            blog.setPlants(plants);
        }

        // Mise à jour image
        if (image != null && !image.isEmpty()) {
            try {
                String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filepath = Paths.get(uploadPath, filename);
                Files.copy(image.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
                blog.setImage("/uploads/" + filename);
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Erreur lors de l’upload de l’image");
            }
        }

        blogRepository.save(blog);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) {
        if (!blogRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        blogRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
