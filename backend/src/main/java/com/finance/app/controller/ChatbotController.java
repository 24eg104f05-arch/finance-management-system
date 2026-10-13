package com.finance.app.controller;

import com.finance.app.dto.chatbot.ChatMessageDto;
import com.finance.app.dto.chatbot.ChatRequest;
import com.finance.app.dto.chatbot.ChatResponse;
import com.finance.app.service.AiChatbotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final AiChatbotService aiChatbotService;

    public ChatbotController(AiChatbotService aiChatbotService) {
        this.aiChatbotService = aiChatbotService;
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> askQuestion(@Valid @RequestBody ChatRequest req) {
        return ResponseEntity.ok(aiChatbotService.processChat(req.getMessage()));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessageDto>> getHistory() {
        return ResponseEntity.ok(aiChatbotService.getChatHistory());
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> clearHistory() {
        aiChatbotService.clearHistory();
        return ResponseEntity.noContent().build();
    }
}
