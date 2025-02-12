package org.example.app.Daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.app.Models.Dtos.MessageDto;
import org.example.app.Models.Entities.Chat;
import org.example.app.Models.Entities.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Repository
public class ChatDao {
    @PersistenceContext
    private EntityManager entityManager;

    public void saveChat(Chat chat) {
        entityManager.persist(chat);
    }


    public void updateChat(Chat chat) {
        entityManager.merge(chat);
    }


    public List<Chat> findByUsername(UUID userId) {
        List<Chat> chats = entityManager.createQuery("SELECT c FROM Chat c WHERE c.userOne = :userId OR c.userTwo = :userId", Chat.class)
                .setParameter("userId", userId)
                .getResultList();
        return chats;
    }


    public List<Message> findMessagesByChat(UUID chatId) {
        List<Message> messages = entityManager.createQuery("SELECT m FROM Message m WHERE m.chat.id = :chatId", Message.class)
                .setParameter("chatId", chatId)
                .getResultList();
        return messages;
    }


    public Chat findChatById(UUID id) {
        return entityManager.find(Chat.class, id);
    }


    public void saveMessage(Message message) {
        entityManager.persist(message);
    }

}
