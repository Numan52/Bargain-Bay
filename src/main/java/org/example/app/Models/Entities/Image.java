package org.example.app.Models.Entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "ad_image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String url;


    public Image() {

    }


    public Image(String url) {
        this.url = url;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
