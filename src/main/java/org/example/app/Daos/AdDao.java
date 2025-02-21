package org.example.app.Daos;

import jakarta.persistence.*;
import org.example.app.Controllers.AuthController;
import org.example.app.Models.Entities.Ad;
import org.example.app.Models.Entities.UserActivity;
import org.example.app.Models.Entities.Image;
import org.example.app.Services.AdsFetching.AdFetchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AdDao {

    private static final Logger logger = LoggerFactory.getLogger(AdDao.class);


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

        for (Ad ad : lastViewedAds) {
            logger.info("last viewed Ad: {}", ad);
        }

        if (lastViewedAds.isEmpty()) {
            // No previous views, return trending ads instead
            return getTrendingAds(filter.getLimit());
        }

        Query query = getPersonalizedAdsQuery(lastViewedAds);
        logger.info("final query: {}", query.toString());

        return query.setMaxResults(filter.getLimit()).getResultList();
    }


    private Query getPersonalizedAdsQuery(List<Ad> lastViewedAds) {
        StringBuilder queryString = new StringBuilder("Select * FROM Ad WHERE ");
        for (int i = 0; i < lastViewedAds.size(); i++) {
            if (i > 0) {
                queryString.append(" OR ");
            }
            queryString.append("(title_tokens || ' ' || description_tokens) @@ to_tsquery('english', :title" + i + ")");
        }

        Query query = entityManager.createNativeQuery(queryString.toString(), Ad.class);

        for (int i = 0; i < lastViewedAds.size(); i++) {
            String title = lastViewedAds.get(i).getTitle();
            query.setParameter("title" + i, title.replaceAll("\\s+", " & "));
        }

        return query;
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
