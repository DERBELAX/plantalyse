package com.example.plantalysBackend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.plantalysBackend.model.PostCommunity;

public interface PostCommunityRepository extends JpaRepository<PostCommunity, Long> {

    // Récupère tous les posts triés par date de création décroissante
	List<PostCommunity> findAllByOrderByCreatedAtDesc();

}

