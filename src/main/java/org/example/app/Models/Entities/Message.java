package org.example.app.Models.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private boolean wasSeen;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private LocalDateTime sentAt;
    private String content;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;


    public Message() {

    }


    public Message(User sender, User receiver, boolean wasSeen, Chat chat, LocalDateTime sentAt, String content, Ad ad) {
        this.sender = sender;
        this.receiver = receiver;
        this.wasSeen = wasSeen;
        this.chat = chat;
        this.sentAt = sentAt;
        this.content = content;
        this.ad = ad;
    }


}
