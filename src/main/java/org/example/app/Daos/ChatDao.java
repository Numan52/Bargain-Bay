package org.example.app.Daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.example.app.Models.Entities.Chat;
import org.example.app.Models.Entities.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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


    public Chat findBySenderAndReceiver(UUID senderId, UUID receiverId) {
        try {
            return entityManager.createQuery(
                            "SELECT c FROM Chat c " +
                                    "WHERE (c.userOne.id = :senderId AND c.userTwo.id = :receiverId) OR " +
                                    "(c.userOne.id = :receiverId AND c.userTwo.id = :senderId)", Chat.class
                    )
                    .setParameter("senderId", senderId)
                    .setParameter("receiverId", receiverId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public List<Chat> findByUser(UUID userId) {
        List<Chat> chats = entityManager.createQuery(
                "SELECT c FROM Chat c WHERE c.userOne.id = :userId OR c.userTwo.id = :userId " +
                        "ORDER BY c.lastMessage.sentAt DESC",
                        Chat.class)
                .setParameter("userId", userId)
                .getResultList();
        return chats;
    }


    public List<Message> findMessagesByChat(UUID chatId) {
        List<Message> messages = entityManager.createQuery(
                "SELECT m FROM Message m WHERE m.chat.id = :chatId " +
                        "ORDER BY m.sentAt ASC",
                        Message.class)
                .setParameter("chatId", chatId)
                .getResultList();
        return messages;
    }


    public Map<UUID, Long> findUnreadMessageCounts(UUID userId) {
        List<Object[]> unreadMessageCounts = (List<Object[]>) entityManager.createQuery(
                "SELECT m.chat.id, COUNT(m) FROM Message m " +
                "WHERE m.receiver.id = :userId AND m.wasSeen = false " +
                "GROUP BY m.chat.id"
                )
                .setParameter("userId", userId)
                .getResultList();

        return unreadMessageCounts.stream()
                .collect(Collectors.toMap(
                        (row) -> (UUID) row[0],
                        (row) -> (Long) row[1]
                ));
    }


    public Chat findChatById(UUID id) {
        return entityManager.find(Chat.class, id);
    }


    public void saveMessage(Message message) {
        entityManager.persist(message);
    }

    public void markMessagesAsRead(UUID chatId, UUID userId) {
        entityManager.createQuery(
                "UPDATE Message m SET m.wasSeen = true " +
                        "WHERE m.chat.id = :chatId AND m.receiver.id = :userId"
        )
                .setParameter("chatId", chatId)
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
