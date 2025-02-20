package org.example.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@TestConfiguration
public class TestConfig {

//    @Bean
//    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
//        // Return the EntityManager using the EntityManagerFactory
//        return entityManagerFactory.createEntityManager();
//    }
//
//    @Bean
//    public EntityManagerFactory entityManagerFactory() {
//        // Provide a basic configuration for an EntityManagerFactory
//        // You can configure your EntityManagerFactory based on the test environment needs
//        return new LocalContainerEntityManagerFactoryBean().getNativeEntityManagerFactory(); // Or another implementation of EntityManagerFactory
//    }

}
