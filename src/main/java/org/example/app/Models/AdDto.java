package org.example.app.Models;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public class AdDto {
    private UUID id;
    private String title;
    private float price;
    private String description;
    private String condition;
    private UUID user_id;
    private String[] imageUrls;

    public AdDto(
            String title,
            float price,
            String description,
            String condition,
            UUID user_id,
            String[] imageUrls
    ) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.condition = condition;
        this.user_id = user_id;
        this.imageUrls = imageUrls;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String[] getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }
}
