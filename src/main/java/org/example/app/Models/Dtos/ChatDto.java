package org.example.app.Models.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private UUID id;
    private LocalDateTime createdAt;
    private UUID userOne;
    private UUID userTwo;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private List<MessageDto> messages;
}
