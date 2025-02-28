package org.example.app.Daos;

import jakarta.persistence.*;
import org.example.app.Controllers.AuthController;
import org.example.app.Models.Dtos.AdSearchResponse;
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


    public AdSearchResponse getAds(int offset, int limit) {
        List<Ad> ads = entityManager.createQuery(
                "SELECT a from Ad a order by a.hasPriority DESC, a.lastBumpedAt DESC", Ad.class
        )
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        Long countRows = entityManager.createQuery(
                "SELECT COUNT(a) from Ad a", Long.class
        )
                .getSingleResult();

        return new AdSearchResponse(ads, countRows);
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

// TODO: create indices on lower(title) and lower(description)


    // TODO: PLACE LIMIT HERE OR NOT?
    public AdSearchResponse getSearchedAds(AdFetchingFilter filter) {
        logger.info("query: {}", filter.getQuery());
        List<Ad> searchedAds = entityManager.createQuery(
                        "SELECT a from Ad a WHERE lower(a.title) LIKE :query OR lower(a.description) LIKE :query", Ad.class
                )
                .setParameter("query", "%" + filter.getQuery().toLowerCase() + "%")
                .setFirstResult(filter.getOffset())
                .setMaxResults(filter.getLimit())
                .getResultList();

        Long countRows = entityManager.createQuery(
                "SELECT COUNT(a) FROM Ad a where lower(a.title) LIKE :query OR lower(a.description) LIKE :query", Long.class
        )
                .setParameter("query", "%" + filter.getQuery().toLowerCase() + "%")
                .getSingleResult();

        return new AdSearchResponse(searchedAds, countRows);
    }


    public AdSearchResponse getAdsByCategory(AdFetchingFilter filter) {
        List<Ad> adsInCategory = entityManager.createNativeQuery(
                        "WITH RECURSIVE category_tree AS (" +
                                    "SELECT id, name, parent_id FROM category " +
                                    "WHERE id = :rootCategoryId " +
                                    "UNION ALL " +
                                    "SELECT c.id, c.name, c.parent_id " +
                                    "FROM category c " +
                                    "INNER JOIN category_tree ct ON c.parent_id = ct.id" +
                                ") " +
                                "SELECT * FROM ad WHERE ad.category_id IN (SELECT id FROM category_tree)",
                        Ad.class
                )
                .setParameter("rootCategoryId", filter.getCategoryId())
                .setFirstResult(filter.getOffset())
                .setMaxResults(filter.getLimit())
                .getResultList();

        Long totalAds = (Long) entityManager.createNativeQuery(
                "WITH RECURSIVE category_tree AS (" +
                        "SELECT id, name, parent_id FROM category " +
                        "WHERE id = :rootCategoryId " +
                        "UNION ALL " +
                        "SELECT c.id, c.name, c.parent_id " +
                        "FROM category c " +
                        "INNER JOIN category_tree ct ON c.parent_id = ct.id" +
                        ") " +
                        "SELECT COUNT(*) FROM ad WHERE ad.category_id IN (SELECT id FROM category_tree)", Long.class
        )
                .setParameter("rootCategoryId", filter.getCategoryId())
                .getSingleResult();

        return new AdSearchResponse(adsInCategory, totalAds);
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
//        query.setMaxResults(filter.getLimit());

        logger.info("final query: {}", query.toString());

        return query.getResultList();
    }


    private Query getPersonalizedAdsQuery(List<Ad> lastViewedAds) {
        StringBuilder queryString = new StringBuilder();
        for (int i = 0; i < lastViewedAds.size(); i++) {
            if (i > 0) {
                queryString.append(" UNION ");
            }
            queryString.append(
                    "(Select * FROM Ad WHERE (title_tokens || ' ' || description_tokens) @@ to_tsquery('english', :title" + i + ") " +
                    "OR category_id = :categoryId" + i + " " +
                    "LIMIT 3)"
            );
        }

        Query query = entityManager.createNativeQuery(queryString.toString(), Ad.class);
        logger.info("query: {}", queryString);

        for (int i = 0; i < lastViewedAds.size(); i++) {
            String title = lastViewedAds.get(i).getTitle();
            UUID categoryId = lastViewedAds.get(i).getCategory().getId();

            query.setParameter("title" + i, title.replaceAll("\\s+", " & "));
            query.setParameter("categoryId" + i, categoryId);
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

    // TODO FINISH
    public void updateAdView(UUID id, UUID adId) {
    }
}
