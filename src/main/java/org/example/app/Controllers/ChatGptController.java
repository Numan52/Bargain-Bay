package org.example.app.Controllers;

import org.example.app.Services.ChatGptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
public class ChatGptController {

    private final ChatGptService openAIService;
    private final static Logger logger = LoggerFactory.getLogger(ChatGptController.class);

    public ChatGptController(ChatGptService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/ai/ask")
    public ResponseEntity<?> chatWithGPT(@RequestBody Map<String, Object> message) {
        logger.info("adId: {}", message.get("adId"));
        UUID adId = message.get("adId") != null && !String.valueOf(message.get("adId")).isBlank() ?
                UUID.fromString((String) message.get("adId")) :
                null;
        return ResponseEntity.ok().body(Map.of("reply", openAIService.getChatResponse((String) message.get("message"), adId)));
    }
}

