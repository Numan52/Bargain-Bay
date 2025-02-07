package org.example.app.Daos;

import jakarta.persistence.*;
import org.example.app.Models.AdDto;
import org.example.app.Models.CreateAdDto;
import org.example.app.Models.Entities.Ad;
import org.example.app.Models.Entities.Image;
import org.example.app.Models.Entities.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AdDao {
    @PersistenceContext
    private EntityManager entityManager;


    public List<Ad> getAds(int offset, int limit) {
        TypedQuery<Ad> query = entityManager.createQuery(
                "SELECT a from Ad a order by a.hasPriority DESC, a.lastBumpedAt DESC", Ad.class
        );
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }


    public Ad getAd(UUID id) {
        return entityManager.find(Ad.class, id);
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
