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
public class ChatContactsDto {
    private UUID chatId;
    private LocalDateTime createdAt;
    private UUID userId;
    private String username;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

}
