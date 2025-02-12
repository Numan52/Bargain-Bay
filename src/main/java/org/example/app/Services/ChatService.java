package org.example.app.Services;

import jakarta.transaction.Transactional;
import org.example.app.Daos.ChatDao;
import org.example.app.Models.Dtos.ChatDto;
import org.example.app.Models.Dtos.MessageDto;
import org.example.app.Models.Entities.Chat;
import org.example.app.Models.Entities.Message;
import org.example.app.Models.Entities.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private ChatDao chatDao;
    private UserService userService;

    public ChatService(ChatDao chatDao, UserService userService) {
        this.chatDao = chatDao;
        this.userService = userService;
    }


    @Transactional
    public Chat findChat(UUID uuid) {
        return chatDao.findChatById(uuid);
    }


    public List<Message> findMessagesByChat(UUID chatId) {
        List<Message> messages = chatDao.findMessagesByChat(chatId);
        return messages;
    }


    @Transactional
    public List<Chat> findChatByUser(UUID userId) {
        return chatDao.findByUsername(userId);
    }


    @Transactional
    public MessageDto saveMessage(MessageDto messageDto) {
        User sender = userService.findUserById(messageDto.getSenderId());
        User receiver = userService.findUserById(messageDto.getReceiverId());
        LocalDateTime time = LocalDateTime.now();

        Message message = new Message(
                sender,
                receiver,
                null,
                time,
                messageDto.getContent()
        );
        chatDao.saveMessage(message);

        Chat chat;

        if (messageDto.getChatId() == null) {
            chat = createChat(message);
        } else {
            chat = findChat(messageDto.getChatId());
            chat.setLastMessage(message);
            chatDao.updateChat(chat);
        }

        message.setChat(chat);
        messageDto.setChatId(chat.getId());

        return messageDto;
    }

    private Chat createChat(Message message) {
        Chat chat = new Chat(
                message.getSentAt(),
                message.getSender(),
                message.getReceiver(),
                message
        );

        chatDao.saveChat(chat);
        return chat;
    }


//    public MessageDto toMessageDto(Message message) {
//        return new MessageDto(
//                message.getId(),
//                message.getSender().getId(),
//                message.getReceiver().getId(),
//                message.getChat().getId(),
//                message.getSentAt(),
//                message.getContent()
//        );
//    }
//
//
//    public ChatDto toChatDto(Chat chat, List<Message> messages) {
//        return new ChatDto(
//                chat.getId(),
//                chat.getCreatedAt(),
//                chat.getUserOne().getId(),
//                chat.getUserTwo().getId(),
//                messages.stream()
//                        .map((message -> toMessageDto(message)))
//                        .toList()
//        );
//    }


}



