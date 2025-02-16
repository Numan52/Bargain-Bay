package org.example.app;

import org.example.app.Daos.AdDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication implements CommandLineRunner {
    private final AdDao adDao;

    public AppApplication(AdDao adDao) {
        this.adDao = adDao;
    }


    public static void main(String[] args) {

        SpringApplication.run(AppApplication.class, args);
    }

    @Override
    public void run(String... args) {

    }


    public void createAds() {

    }


    public void createAdImages() {

    }

}
