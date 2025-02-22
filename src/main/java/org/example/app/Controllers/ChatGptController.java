package org.example.app.Controllers;

import org.example.app.Services.ChatGptService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class ChatGptController {

    private final ChatGptService openAIService;

    public ChatGptController(ChatGptService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/ai/ask")
    public String chatWithGPT(@RequestBody Map<String, String> message) {
        return openAIService.getChatResponse(message.get("message"));
    }
}

