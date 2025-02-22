package org.example.app.Controllers;

import org.example.app.Services.ChatGptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
public class ChatGptController {

    private final ChatGptService openAIService;

    public ChatGptController(ChatGptService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/ai/ask")
    public ResponseEntity<?> chatWithGPT(@RequestBody Map<String, Object> message) {
        return ResponseEntity.ok().body(Map.of("reply", openAIService.getChatResponse((String) message.get("message"), UUID.fromString((String) message.get("adId")))));
    }
}

