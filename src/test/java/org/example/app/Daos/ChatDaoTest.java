package org.example.app.Daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.app.Models.Entities.Chat;
import org.example.app.Models.Entities.User;
import org.example.app.RoleType;
import org.example.app.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;



@DataJpaTest
@Import({UserDao.class, ChatDao.class})
class ChatDaoTest {

    @Autowired
    private ChatDao chatDao;

    @Autowired
    private UserDao userDao;


    private User userOne;
    private User userTwo;
    private Chat chat;

    @BeforeEach
    void setUp() {
        // Create test users
        userOne = new User(
                "Anna",
                "Smith",
                "anna22",
                "passowrd",
                "anna@gmail.com",
                new ArrayList<>()
        );


        userTwo = new User(
                "Jakob",
                "Wiemann",
                "jakob22",
                "passowrd",
                "jakob22@gmail.com",
                new ArrayList<>()
        );

        userDao.saveUser(userOne);
        userDao.saveUser(userTwo);

        // Create test chat
        chat = new Chat();
        chat.setUserOne(userOne);
        chat.setUserTwo(userTwo);

        chatDao.saveChat(chat);
    }


    @Test
    void findsChatBySenderAndReceiver() {
        Chat foundChat = chatDao.findBySenderAndReceiver(userOne.getId(), userTwo.getId());

        assertThat(foundChat.getId()).isEqualTo(chat.getId());
    }
}