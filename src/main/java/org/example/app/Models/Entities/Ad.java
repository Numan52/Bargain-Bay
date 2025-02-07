package org.example.app.Models.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private float price;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String condition;
    @Column(nullable = false)
    private boolean hasPriority;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastBumpedAt; // used for sorting ads

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany()
    @JoinColumn(name = "ad_id")
    private List<Image> images;

    public Ad() {
    }


    public Ad(
            String title,
            float price,
            String description,
            String condition,
            LocalDateTime createdAt,
            LocalDateTime lastBumpedAt,
            User user,
            List<Image> images,
            boolean hasPriority
    ) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.condition = condition;
        this.createdAt = createdAt;
        this.lastBumpedAt = lastBumpedAt;
        this.user = user;
        this.images = images;
        this.hasPriority = hasPriority;
    }


    public UUID getId() {
        return id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> imageUrls) {
        this.images = imageUrls;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime dateTime) {
        this.createdAt = dateTime;
    }

    public boolean isHasPriority() {
        return hasPriority;
    }

    public void setHasPriority(boolean hasPriority) {
        this.hasPriority = hasPriority;
    }

    public LocalDateTime getLastBumpedAt() {
        return lastBumpedAt;
    }

    public void setLastBumpedAt(LocalDateTime lastBumpedAt) {
        this.lastBumpedAt = lastBumpedAt;
    }
}
