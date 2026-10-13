package com.finance.app.dto.chatbot;

import com.finance.app.entity.ChatMessage;
import java.time.LocalDateTime;

public class ChatMessageDto {
    private Long id;
    private String role; // "user" or "assistant"
    private String content;
    private LocalDateTime createdAt;

    public ChatMessageDto() {}

    public ChatMessageDto(ChatMessage msg) {
        this.id = msg.getId();
        this.role = msg.getRole();
        this.content = msg.getContent();
        this.createdAt = msg.getCreatedAt();
    }

    public ChatMessageDto(String role, String content) {
        this.role = role;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
