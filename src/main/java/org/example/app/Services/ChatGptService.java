package org.example.app.Services;

import org.example.app.Controllers.AdController;
import org.example.app.Models.Entities.Ad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class ChatGptService {
    private final static Logger logger = LoggerFactory.getLogger(ChatGptService.class);
    private final AdService adService;
    private final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${OPENAI_API_KEY}")
    private String API_KEY;

    public ChatGptService(AdService adService) {
        this.adService = adService;
    }

    public String getChatResponse(String userMessage, UUID adId) {
        Ad ad = adService.getAd(adId);
        RestTemplate restTemplate = new RestTemplate();
        String requestMessage = userMessage;

        if (adId != null) {
            requestMessage += ". Also here are some infos about the ad: " + "title: " + ad.getTitle() + ". Description: " + ad.getDescription() + ". Price: " + ad.getPrice();
        }

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("max_tokens", 100);
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "You are a helpful assistant that is helping users buy and sell products on an online marketplace. Keep responses and under 100 tokens."),
                Map.of("role", "user", "content", requestMessage)
        ));

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        logger.info("user message: {}", userMessage);

        // Make request
        ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, Map.class);

        logger.info("chatgpt response: {}", response.getBody().toString());


        // Extract response text
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return (String) message.get("content");
    }
}

