package org.example.app.Controller;

import org.example.app.Daos.AdDao;
import org.example.app.Models.Dtos.AdSearchResponse;
import org.example.app.Models.Entities.Ad;
import org.example.app.Models.Entities.User;
import org.example.app.Services.AdsFetching.AdFetchingFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class AdControllerTest {
    @LocalServerPort
    private int port;

    @MockitoBean
    private AdDao adDao;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testGetsAllAdsByCategoryElectronics() throws Exception {
        UUID categoryId = UUID.randomUUID();
        String url = "http://localhost:" + port + "/ads/filtered-by-category?categoryId="+ categoryId + "&offset=0&limit=10";

        User user = new User();
        user.setFirstName("user1");
        user.setId(UUID.randomUUID());

        Ad ad1 = new Ad();
        ad1.setTitle("Title 1");
        ad1.setUser(user);
        ad1.setImages(List.of());

        Ad ad2 = new Ad();
        ad2.setTitle("Title 2");
        ad2.setUser(user);
        ad2.setImages(List.of());

        List<Ad> mockAds = List.of(ad1, ad2);

        AdSearchResponse adSearchResponse = new AdSearchResponse(mockAds, 2);
        System.out.println(adSearchResponse);
        Mockito.when(adDao.getAdsByCategory(Mockito.any(AdFetchingFilter.class)))
                .thenReturn(adSearchResponse);


        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ads.length()").value(2))
                .andExpect(jsonPath("$.ads[0].title").value("Title 1"))
                .andExpect(jsonPath("$.ads[1].title").value("Title 2"));

    }


}
