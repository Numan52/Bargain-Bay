package org.example.app.Daos;

import jakarta.persistence.*;
import org.example.app.Models.Entities.Ad;
import org.example.app.Models.Entities.AdView;
import org.example.app.Models.Entities.Image;
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


    public List<Ad> getTrendingAds(int limit) {
        List<Ad> trendingAds = entityManager.createQuery(
                "SELECT a from Ad a ORDER BY a.viewsCount DESC ", Ad.class
        )
                .setMaxResults(limit)
                .getResultList();
        return trendingAds;
    }


    public List<Ad> getFreshAds(int limit) {
        List<Ad> freshAds = entityManager.createQuery(
                        "SELECT a from Ad a ORDER BY a.createdAt DESC ", Ad.class
                )
                .setMaxResults(limit)
                .getResultList();
        return freshAds;
    }


    public void updateViewsCount(UUID adId) {
        entityManager.createQuery("UPDATE Ad a SET a.viewsCount = a.viewsCount + 1 where a.id = :adId")
                .setParameter("adId", adId)
                .executeUpdate();
    }


    public boolean checkIfUserSeen(UUID userId, UUID adId) {
        boolean hasSeen = (boolean) entityManager.createNativeQuery(
                "SELECT EXISTS(SELECT av.user_id FROM ad_view av WHERE av.user_id = :userId and av.ad_id = :adId) "
        )
                .setParameter("adId", adId)
                .setParameter("userId", userId)
                .getSingleResult();

        return hasSeen;
    }


    public boolean checkIfGuestSeen(String ipAddress, UUID adId) {
        boolean hasSeen = (boolean) entityManager.createNativeQuery(
                        "SELECT EXISTS(SELECT av.user_id FROM ad_view av WHERE av.ip_address = :ipAddress and av.ad_id = :adId) "
                )
                .setParameter("adId", adId)
                .setParameter("ipAddress", ipAddress)
                .getSingleResult();

        return hasSeen;
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

    public void saveAdView(AdView adView) {
        entityManager.persist(adView);
    }

}
