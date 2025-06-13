package com.example.plantalysBackend.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "message")
public class Message {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_message;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdat;

    @ManyToOne
    @JoinColumn(name = "id_message_parent")
    private Message parentMessage;

    @OneToMany(mappedBy = "parentMessage")
    private List<Message> replies;

	public Message(Long id_message, String content, LocalDateTime createdat, Message parentMessage,
			List<Message> replies) {
		super();
		this.id_message = id_message;
		this.content = content;
		this.createdat = createdat;
		this.parentMessage = parentMessage;
		this.replies = replies;
	}

	public Message() {
		super();
	}
	public Long getId_message() {
		return id_message;
	}

	public void setId_message(Long id_message) {
		this.id_message = id_message;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedat() {
		return createdat;
	}

	public void setCreatedat(LocalDateTime createdat) {
		this.createdat = createdat;
	}

	public Message getParentMessage() {
		return parentMessage;
	}

	public void setParentMessage(Message parentMessage) {
		this.parentMessage = parentMessage;
	}

	public List<Message> getReplies() {
		return replies;
	}

	public void setReplies(List<Message> replies) {
		this.replies = replies;
	}

}
