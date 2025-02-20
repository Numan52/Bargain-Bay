package org.example.app.Models.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;


    private String search_query;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type",  nullable = false)
    private UserAdInteraction actionType;

    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime timestamp;


    public UserActivity() {
    }


    public UserActivity(User user, Ad ad, String search_query, UserAdInteraction actionType, String ipAddress) {
        this.user = user;
        this.ad = ad;
        this.search_query = search_query;
        this.actionType = actionType;
        this.ipAddress = ipAddress;
        this.timestamp = LocalDateTime.now();
    }

    // for ad views
    public UserActivity(User user, Ad ad, String ipAddress) {
        this(user, ad, null, UserAdInteraction.VIEW, ipAddress);
    }

    // for searches
    public UserActivity(User user, String ipAddress, String searchQuery) {
        this(user, null, searchQuery, UserAdInteraction.SEARCH, ipAddress);
    }


}
