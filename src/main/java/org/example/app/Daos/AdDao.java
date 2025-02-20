package org.example.app.Daos;

import jakarta.persistence.*;
import org.example.app.Models.Entities.Ad;
import org.example.app.Models.Entities.UserActivity;
import org.example.app.Models.Entities.Image;
import org.example.app.Services.AdsFetching.AdFetchingFilter;
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

//    // TODO: create indices on lower(title) and lower(description)
//    public List<Ad> getSearchedAds(AdFetchingFilter filter) {
//        List<Ad> freshAds = entityManager.createQuery(
//                        "SELECT a from Ad a WHERE lower(a.title) LIKE :query OR lower(a.description) LIKE :query", Ad.class
//                )
//                .setParameter("query", "%" + filter.getQuery().toLowerCase() + "%")
//                .setFirstResult(filter.getOffset())
//                .setMaxResults(filter.getLimit())
//                .getResultList();
//        return freshAds;
//    }

    // TODO: PLACE LIMIT HERE OR NOT?
    public List<Ad> getSearchedAds(AdFetchingFilter filter) {
        List<Ad> searchedAds = entityManager.createQuery(
                        "SELECT a from Ad a WHERE lower(a.title) LIKE :query OR lower(a.description) LIKE :query", Ad.class
                )
                .setParameter("query", "%" + filter.getQuery().toLowerCase() + "%")
                .getResultList();
        return searchedAds;
    }


    public List<Ad> getPersonalizedAds(AdFetchingFilter filter) {
        List<Ad> lastViewedAds = entityManager.createQuery(
                "SELECT ua.ad FROM UserActivity ua WHERE ua.user.id = :userId ORDER BY ua.timestamp DESC", Ad.class
        )
                .setParameter("userId", filter.getUserId())
                .setMaxResults(5)
                .getResultList();

        if (lastViewedAds.isEmpty()) {
            // No previous views, return trending ads instead
            return getTrendingAds(filter.getLimit());
        }

        AdFetchingFilter filter = new AdFetchingFilter.Builder().query()

        List<Ad> personalizedAds = getSearchedAds();
        return personalizedAds;
    }


    public void updateViewsCount(UUID adId) {
        entityManager.createQuery("UPDATE Ad a SET a.viewsCount = a.viewsCount + 1 where a.id = :adId")
                .setParameter("adId", adId)
                .executeUpdate();
    }


    public boolean checkIfUserSeen(UUID userId, UUID adId) {
        boolean hasSeen = (boolean) entityManager.createNativeQuery(
                "SELECT EXISTS(SELECT ua.user_id FROM user_activity ua WHERE ua.user_id = :userId and ua.ad_id = :adId) "
        )
                .setParameter("adId", adId)
                .setParameter("userId", userId)
                .getSingleResult();

        return hasSeen;
    }


    public boolean checkIfGuestSeen(String ipAddress, UUID adId) {
        boolean hasSeen = (boolean) entityManager.createNativeQuery(
                        "SELECT EXISTS(SELECT ua.user_id FROM user_activity ua WHERE ua.ip_address = :ipAddress and ua.ad_id = :adId) "
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

    public void saveAdView(UserActivity userActivity) {
        entityManager.persist(userActivity);
    }


}
