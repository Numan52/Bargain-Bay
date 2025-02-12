package org.example.app.Models.Dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    private UUID senderId;
    private UUID receiverId;
    private UUID chatId;
    private LocalDateTime sentAt;
    private String content;
}
