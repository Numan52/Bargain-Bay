package org.example.app.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.app.Exceptions.ExceptionUtil;
import org.example.app.Models.Dtos.AdDto;
import org.example.app.Models.Dtos.ChatContactsDto;
import org.example.app.Models.Dtos.ChatDto;
import org.example.app.Models.Dtos.MessageDto;
import org.example.app.Models.Entities.Ad;
import org.example.app.Models.Entities.Chat;
import org.example.app.Models.Entities.Message;
import org.example.app.Models.Entities.User;
import org.example.app.Services.AdService;
import org.example.app.Services.ChatService;
import org.example.app.Services.UserService;
import org.example.app.Services.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController

public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final UserService userService;
    private final AdService adService;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService, UserService userService, AdService adService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.userService = userService;
        this.adService = adService;
    }


    @GetMapping("/chats/{chatId}/messages")
    public ResponseEntity<?> getChat(@PathVariable UUID chatId, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User currentUser = userService.findUserByName(username);
        Chat chat= chatService.findChat(chatId);

            if (!chat.getUserOne().getId().equals(currentUser.getId()) && !chat.getUserTwo().getId().equals(currentUser.getId())) {
            logger.warn("User {} is not allowed to view chat {}", currentUser.getId(), chat.getId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtil.buildErrorResponse(HttpStatus.FORBIDDEN,
                    "You are not allowed to view this chat", "/chats/" + chatId +  "/messages"));
        }

        List<Message> messages = chatService.findMessagesByChat(chat.getId());

        List<MessageDto> messageDtos = messages.stream().map(message -> {
            AdDto adDto = null;
            if (message.getAd() != null) {
                Ad ad = adService.getAd(message.getAd().getId());
                adDto = Utils.toAdDto(ad);
            }
            return new MessageDto(
                    message.getId(),
                    message.getSender().getId(),
                    message.getReceiver().getId(),
                    false,
                    message.getChat().getId(),
                    message.getSentAt(),
                    message.getContent(),
                    adDto
            );
        }).toList();

        ChatDto chatDto = new ChatDto(
                chat.getId(),
                chat.getCreatedAt(),
                chat.getUserOne().getId(),
                chat.getUserTwo().getId(),
                chat.getLastMessage().getContent(),
                chat.getLastMessage().getSentAt(),
                messageDtos
        );

        return ResponseEntity.ok(chatDto);

    }


    @GetMapping("/chats/contacts")
    public ResponseEntity<?> getChatContacts(HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");
            User currentUser = userService.findUserByName(username);
            List<Chat> chats = chatService.findChatByUser(currentUser.getId());
            Map<UUID, Long> unreadCounts = chatService.getUnreadMessageCounts(currentUser.getId());
            List<ChatContactsDto> contacts = chats.stream()
                    .map((chat -> {
                        User other = chat.getUserOne().getId().equals(currentUser.getId()) ?
                                chat.getUserTwo() : chat.getUserOne();
                        return new ChatContactsDto(
                                chat.getId(),
                                chat.getCreatedAt(),
                                other.getId(),
                                other.getUsername(),
                                chat.getLastMessage().getContent(),
                                chat.getLastMessage().getSentAt(),
                                unreadCounts.getOrDefault(chat.getId(), 0L)
                        );
                    }))
                    .toList();

            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            logger.error("error getting chats: ", e);
            throw e;
        }
    }


    @MessageMapping("/chat")
    public void processMessage(@Payload MessageDto messageDto, @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) throws Exception {
        try {
            logger.info("session attributes in messageMapping: {}", sessionAttributes);
            UUID senderId = userService.findUserByName((String) sessionAttributes.get("username")).getId();
            if (!messageDto.getSenderId().equals(senderId)) {
                throw new Exception("Unauthorized: Sender ID does not match authenticated user");
            }
            

            MessageDto savedMsgDto = chatService.saveMessage(messageDto);
            logger.info("saved msg dto: {}", savedMsgDto.toString());
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(messageDto.getReceiverId()),
                    "/queue/messages",
                    savedMsgDto
            );

            // Send the message back to the sender
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(senderId),
                    "/queue/messages",
                    savedMsgDto
            );
        } catch (Exception e) {
            logger.error("error handling message: ", e);
            throw e;
        }
    }

    @PostMapping("/chats/{chatId}/seen")
    public ResponseEntity<?> markMessagesAsRead(@PathVariable UUID chatId, HttpServletRequest request) throws Exception {
        String username = (String) request.getAttribute("username");
        User currentUser = userService.findUserByName(username);
        try {
            chatService.markMessagesAsRead(chatId, currentUser.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("error updating messages as read: ", e);
            throw new Exception("Unexpected Error occurred");
        }
    }

}
