package org.example.app.Security;

import org.example.app.Models.AdDto;
import org.example.app.Models.Entities.Ad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.stream.Collectors;

@Configuration
public class Config {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Allow all endpoints
                        .allowedOrigins("http://localhost:5173")  // Allow frontend URL
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow HTTP methods
                        .allowedHeaders("*")
                        .allowCredentials(true);  // Allow cookies/auth headers
            }
        };
    }



}
