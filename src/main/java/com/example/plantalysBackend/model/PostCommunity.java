package com.example.plantalysBackend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "post_community")
public class PostCommunity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_post_community;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdat;

    private String image;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

	public PostCommunity(Long id_post_community, String title, String description, LocalDateTime createdat,
			String image, User user) {
		super();
		this.id_post_community = id_post_community;
		this.title = title;
		this.description = description;
		this.createdat = createdat;
		this.image = image;
		this.user = user;
	}

	public PostCommunity() {
		super();
	}
	public Long getId_post_community() {
		return id_post_community;
	}

	public void setId_post_community(Long id_post_community) {
		this.id_post_community = id_post_community;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreatedat() {
		return createdat;
	}

	public void setCreatedat(LocalDateTime createdat) {
		this.createdat = createdat;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
