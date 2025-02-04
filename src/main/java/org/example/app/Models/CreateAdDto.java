package org.example.app.Models;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public class CreateAdDto {
    private String title;
    private float price;
    private String description;
    private String condition;
    private String username;
    private List<MultipartFile> images;


    public CreateAdDto(String title, float price, String description, String condition, String username, List<MultipartFile> images) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.condition = condition;
        this.username = username;
        this.images = images;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "CreateAdDto{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", condition='" + condition + '\'' +
                ", username='" + username + '\'' +
                ", images=" + images +
                '}';
    }
}
