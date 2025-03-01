package org.example.app.Daos;

import jakarta.transaction.Transactional;
import org.example.app.Models.Dtos.AdSearchResponse;
import org.example.app.Models.Entities.Ad;
import org.example.app.Models.Entities.Category;
import org.example.app.Models.Entities.Chat;
import org.example.app.Models.Entities.User;
import org.example.app.Services.AdsFetching.AdFetchingFilter;
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

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Rollback
@DataJpaTest
@Import({UserDao.class, AdDao.class})
public class AdDaoTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer(
            "postgres:16-alpine"
    );


    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @Autowired
    private AdDao adDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestEntityManager testEntityManager;

    private Category electronicsCategory;


    @BeforeEach
    void setUp() {
        User userOne = new User();
        userOne.setFirstName("Martin");
        userOne.setLastName("Smith");
        userOne.setPassword("password");
        userOne.setUsername("martin11");
        userOne.setEmail("martin@gmail.com");
        testEntityManager.persist(userOne);

        electronicsCategory = new Category(null, "Electronics");
        testEntityManager.persist(electronicsCategory);

        Category laptopsCategory = new Category(electronicsCategory, "Laptops");
        testEntityManager.persist(laptopsCategory);

        Category booksCategory = new Category(null, "Books");
        testEntityManager.persist(booksCategory);

        Ad ad1 = new Ad(
                "Book1",
                10,
                "book description",
                "new",
                LocalDateTime.now(),
                booksCategory,
                LocalDateTime.now(),
                userOne,
                null,
                false
        );
        testEntityManager.persist(ad1);

        Ad ad2 = new Ad(
                "Gaming laptop 1",
                500,
                "Gaming laptop description",
                "new",
                LocalDateTime.now(),
                laptopsCategory,
                LocalDateTime.now(),
                userOne,
                List.of(),
                false
        );
        testEntityManager.persist(ad2);

        Ad ad3= new Ad(
                "Gaming laptop 2",
                600,
                "Gaming laptop description 2",
                "new",
                LocalDateTime.now(),
                laptopsCategory,
                LocalDateTime.now(),
                userOne,
                List.of(),
                false
        );
        testEntityManager.persist(ad3);

        Ad ad4= new Ad(
                "Printer",
                600,
                "Printer description",
                "new",
                LocalDateTime.now(),
                electronicsCategory,
                LocalDateTime.now(),
                userOne,
                List.of(),
                false
        );
        testEntityManager.persist(ad4);

    }


    @Test

    void findsAllAdsInsideElectronicsCategory() {
        AdFetchingFilter filter = new AdFetchingFilter.Builder()
                .category(electronicsCategory.getId()).limit(10).offset(0).build();
        System.out.println(electronicsCategory.toString());
        AdSearchResponse ads = adDao.getAdsByCategory(filter);

        assertThat(ads.getAds()).extracting(adDto -> adDto.getTitle())
                .containsExactlyInAnyOrder("Printer", "Gaming laptop 2", "Gaming laptop 1");
    }
}
