package org.example.app.Services;

import jakarta.transaction.Transactional;
import org.example.app.Daos.ChatDao;
import org.example.app.Models.Dtos.MessageDto;
import org.example.app.Models.Entities.Chat;
import org.example.app.Models.Entities.Message;
import org.example.app.Models.Entities.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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


    public Chat findChatByUsers(UUID senderId, UUID receiverId) {
        return chatDao.findBySenderAndReceiver(senderId, receiverId);
    }


    @Transactional
    public List<Message> findMessagesByChat(UUID chatId) {
        List<Message> messages = chatDao.findMessagesByChat(chatId);
        return messages;
    }


    @Transactional
    public List<Chat> findChatByUser(UUID userId) {
        return chatDao.findByUser(userId);
    }


    @Transactional
    public MessageDto saveMessage(MessageDto messageDto) {
        User sender = userService.findUserById(messageDto.getSenderId());
        User receiver = userService.findUserById(messageDto.getReceiverId());
        LocalDateTime time = LocalDateTime.now();

        Message message = new Message(
                sender,
                receiver,
                false,
                null,
                time,
                messageDto.getContent()
        );
        chatDao.saveMessage(message);

        Chat chat = messageDto.getChatId() != null
                ? findChat(messageDto.getChatId())
                : findChatByUsers(messageDto.getSenderId(), messageDto.getReceiverId());

        if (chat == null) {
            chat = createChat(message);
        }


        chat.setLastMessage(message);
        chatDao.updateChat(chat);


        message.setChat(chat);
        messageDto.setChatId(chat.getId());
        messageDto.setId(message.getId());
        messageDto.setSentAt(message.getSentAt());

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

    @Transactional
    public Map<UUID, Long> getUnreadMessageCounts(UUID userId) {
        return chatDao.findUnreadMessageCounts(userId);
    }


    @Transactional
    public void markMessagesAsRead(UUID chatId, UUID userId) {
        chatDao.markMessagesAsRead(chatId, userId);
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



