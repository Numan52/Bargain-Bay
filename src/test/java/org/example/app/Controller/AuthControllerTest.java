package org.example.app.Controller;


import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.example.app.Daos.UserDao;
import org.example.app.TestContainerConfig;
import org.hibernate.annotations.processing.SQL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.print.attribute.standard.Media;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@Import(TestContainerConfig.class)
@AutoConfigureMockMvc
@Sql(value = {"/schema.sql", "/testData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)

public class AuthControllerTest {
    @LocalServerPort
    private int port;

    @MockitoBean
    private UserDao userDao;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void UserCanLogin() throws Exception {
        Gson gson = new Gson();
        System.out.println(userDao.findUserByUsername("martin11"));
        String url = "http://localhost:" + port + "/login";

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "martin11");
        loginRequest.put("password", "password");
        String json = gson.toJson(loginRequest);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").isNotEmpty());

    }


}
