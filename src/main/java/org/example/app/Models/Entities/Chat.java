package org.example.app.Models.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_one_id")
    private User userOne;

    @ManyToOne()
    @JoinColumn(name = "user_two_id")
    private User userTwo;

    @OneToOne
    @JoinColumn(name = "last_message_id")
    private Message lastMessage;


    public Chat() {
    }

    public Chat(LocalDateTime createdAt, User userOne, User userTwo, Message lastMessage) {
        this.createdAt = createdAt;
        this.userOne = userOne;
        this.userTwo = userTwo;
        this.lastMessage = lastMessage;
    }
}
