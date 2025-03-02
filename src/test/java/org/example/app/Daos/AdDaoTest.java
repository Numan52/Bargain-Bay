package org.example.app.Daos;

import jakarta.transaction.Transactional;
import org.example.app.Models.Dtos.AdSearchResponse;
import org.example.app.Models.Entities.Ad;
import org.example.app.Models.Entities.Category;
import org.example.app.Models.Entities.Chat;
import org.example.app.Models.Entities.User;
import org.example.app.Services.AdsFetching.AdFetchingFilter;
import org.example.app.TestContainerConfig;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import({UserDao.class, AdDao.class, TestContainerConfig.class})
public class AdDaoTest {

    @Autowired
    private AdDao adDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestEntityManager testEntityManager;



    @Test
    void findsAllAdsInsideElectronicsCategory() {

        AdFetchingFilter filter = new AdFetchingFilter.Builder()
                .category(UUID.fromString("aefb83ad-4c9f-4c6a-b599-d8f67f4a1676")).limit(10).offset(0).build();

        AdSearchResponse ads = adDao.getAdsByCategory(filter);

        assertThat(ads.getAds()).extracting(adDto -> adDto.getTitle())
                .containsExactlyInAnyOrder("Printer", "Gaming laptop 2", "Gaming laptop 1");
    }



    @Test
    void findsLaptopsWhenSearchQueryIsLaptop() {
        AdFetchingFilter filter = new AdFetchingFilter.Builder()
                .query("Laptop").limit(10).offset(0).build();

        AdSearchResponse ads = adDao.getSearchedAds(filter);

        assertThat(ads.getAds()).extracting(adDto -> adDto.getTitle())
                .containsExactlyInAnyOrder("Gaming laptop 2", "Gaming laptop 1");
    }
}
