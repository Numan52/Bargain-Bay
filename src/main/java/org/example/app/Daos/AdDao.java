package org.example.app.Daos;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.app.Models.AdDto;
import org.example.app.Models.CreateAdDto;
import org.example.app.Models.Entities.Ad;
import org.example.app.Models.Entities.Image;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdDao {
    @PersistenceContext
    private EntityManager entityManager;


    public void getAds() {
        entityManager.persist();
    }

    public void postAd(Ad ad) {
       entityManager.persist(ad);
    }

    public void saveImages(List<Image> images) {
        for (Image image : images) {
            entityManager.persist(image);
        }
    }

}
