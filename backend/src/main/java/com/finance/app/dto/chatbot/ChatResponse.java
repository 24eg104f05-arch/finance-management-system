package com.finance.app.dto.chatbot;

public class ChatResponse {
    private String reply;
    private Boolean fallbackUsed;

    public ChatResponse() {}

    public ChatResponse(String reply, Boolean fallbackUsed) {
        this.reply = reply;
        this.fallbackUsed = fallbackUsed != null ? fallbackUsed : false;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Boolean getFallbackUsed() {
        return fallbackUsed;
    }

    public void setFallbackUsed(Boolean fallbackUsed) {
        this.fallbackUsed = fallbackUsed;
    }
}
