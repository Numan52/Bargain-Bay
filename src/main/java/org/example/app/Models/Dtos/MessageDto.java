package org.example.app.Models.Dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageDto {
    private UUID id;
    private UUID senderId;
    private UUID receiverId;
    private boolean wasSeen;
    private UUID chatId;
    private LocalDateTime sentAt;
    private String content;
    private AdDto ad;
}
