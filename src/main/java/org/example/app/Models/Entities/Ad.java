package org.example.app.Models.Entities;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;

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

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany()
    @JoinColumn(name = "ad_id")
    private List<Image> imageUrls;

    public Ad() {
    }


    public Ad(String title, float price, String description, String condition, User user, List<Image> imageUrls) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.condition = condition;
        this.user = user;
        this.imageUrls = imageUrls;
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

    public List<Image> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<Image> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
