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
import com.example.plantalysBackend.dto.PlantDTO;
import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
        List<Blog> blogs = blogRepository.findAll();

        List<Map<String, Object>> response = blogs.stream().map(blog -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id_blog", blog.getId_blog());
            map.put("title", blog.getTitle());
            map.put("description", blog.getDescription());
            map.put("image", blog.getImage());
            map.put("createdat", blog.getCreatedat());
            map.put("updatedat", blog.getUpdatedat());

            // User (éviter boucle infinie aussi)
            if (blog.getUser() != null) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("email", blog.getUser().getEmail());
                map.put("user", userMap);
            }

            // Plantes liées (noms uniquement)
            List<PlantDTO> plants = blog.getPlants() != null
            	    ? blog.getPlants().stream()
            	        .map(p -> new PlantDTO(p.getId_plante(), p.getName(), p.getImages()))
            	        .toList()
            	    : List.of();

            map.put("plants", plants);

            return map;
        }).toList();

        return ResponseEntity.ok(response);
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

        // Upload l’image
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
