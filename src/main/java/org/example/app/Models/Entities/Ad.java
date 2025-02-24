package org.example.app.Models.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private float price;

    // TODO: check for errors regarding length
    @Column(nullable = false, length = 1000)
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

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;


    private int viewsCount = 0;


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


}
