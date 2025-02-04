package org.example.app.Daos;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.app.Models.AdDto;
import org.example.app.Models.CreateAdDto;
import org.example.app.Models.Entities.Ad;
import org.springframework.stereotype.Repository;

@Repository
public class AdDao {
    @PersistenceContext
    private EntityManager entityManager;


    public void postAd(Ad ad) {
       entityManager.persist(ad);

    }

}
