package org.example.app.Services;

import org.example.app.Daos.ChatDao;
import org.example.app.Models.Dtos.MessageDto;
import org.example.app.Models.Entities.Chat;
import org.example.app.Models.Entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    private ChatService chatService;

    @Mock
    private ChatDao chatDao;
    @Mock
    private UserService userService;

    @Mock
    private AdService adService;

    private User userOne;
    private User userTwo;

    @BeforeEach
    void setUp() {
        chatService = new ChatService(chatDao, userService, adService);

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
    }


    @Test
    void createsChatIfFirstMessage() {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        User sender = userOne;
        User receiver = userTwo;
        MessageDto messageDto = new MessageDto(null, senderId, receiverId, false,
                null, null, "Hello", null);

        when(userService.findUserById(senderId)).thenReturn(sender);
        when(userService.findUserById(receiverId)).thenReturn(receiver);

        ArgumentCaptor<Chat> chatCaptor = ArgumentCaptor.forClass(Chat.class);

        //when
        messageDto = chatService.saveMessage(messageDto);

        verify(chatDao).saveChat(chatCaptor.capture());

        Chat createdChat = chatCaptor.getValue();

        // then
        assertThat(createdChat.getUserOne()).isIn(List.of(userOne, userTwo));
        assertThat(createdChat.getUserTwo()).isIn(List.of(userOne, userTwo));
        assertThat(messageDto.getContent()).isEqualTo("Hello");
    }



}
