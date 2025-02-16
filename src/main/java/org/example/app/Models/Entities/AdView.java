package org.example.app.Models.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class AdView {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    private String ipAddress;


    public AdView(User user, Ad ad, String ipAddress) {
        this.user = user;
        this.ad = ad;
        this.ipAddress = ipAddress;
    }


    public AdView() {

    }
}
