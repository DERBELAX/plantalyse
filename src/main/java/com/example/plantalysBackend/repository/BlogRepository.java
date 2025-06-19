package com.example.plantalysBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.plantalysBackend.model.Blog;


public interface BlogRepository extends JpaRepository<Blog, Long>{

}
