package com.example.plantalysBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.plantalysBackend.repository.BlogRepository;
import com.example.plantalysBackend.model.Blog;
@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "http://localhost:3000")
public class BlogController {
	@Autowired
    private BlogRepository blogRepository;

    @GetMapping
    public List<Blog> getAll() {
        return blogRepository.findAll();
    }

}
