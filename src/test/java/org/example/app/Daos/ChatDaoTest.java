package org.example.app.Daos;

import org.example.app.Models.Entities.Chat;
import org.example.app.Models.Entities.User;
import org.example.app.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import({UserDao.class, ChatDao.class})
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
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