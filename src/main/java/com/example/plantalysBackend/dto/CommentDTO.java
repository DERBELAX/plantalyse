package com.example.plantalysBackend.dto;

public class CommentDTO {
    private Long postId;
    private String content;

   
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

