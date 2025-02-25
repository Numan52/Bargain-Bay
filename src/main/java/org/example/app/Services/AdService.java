package org.example.app.Services;

import jakarta.transaction.Transactional;
import org.example.app.Daos.AdDao;
import org.example.app.Daos.CategoryDao;
import org.example.app.Daos.UserDao;
import org.example.app.Exceptions.UserException;
import org.example.app.Models.Dtos.AdDto;
import org.example.app.Models.Dtos.AdSearchResponse;
import org.example.app.Models.Dtos.CreateAdDto;
import org.example.app.Models.Entities.*;
import org.example.app.Services.AdsFetching.AdFetchStrategy;
import org.example.app.Services.AdsFetching.AdFetchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdService {
    private final CategoryDao categoryDao;
    private AdDao adDao;
    private UserDao userDao;
    private S3Service s3Service;


    private static final Logger logger = LoggerFactory.getLogger(AdService.class);


    public AdService(AdDao adDao, UserDao userDao, S3Service s3Service, CategoryDao categoryDao) {
        this.adDao = adDao;
        this.userDao = userDao;
        this.s3Service = s3Service;
        this.categoryDao = categoryDao;
    }


     @Transactional
     public Ad getAd(UUID id) {
        return adDao.getAd(id);
     }


    @Transactional
    public List<Ad> getAds(AdFetchStrategy fetchStrategy, AdFetchingFilter filter) {
        return fetchStrategy.fetchAds(filter);
    }

    //TODO: REWRITE
    public AdSearchResponse getSearchedAds(AdFetchingFilter filter) {
        List<Ad> ads = adDao.getSearchedAds(filter);
        logger.info("total ads legnth: {}", ads.size());

        int lastAdIndex = filter.getOffset() + filter.getLimit() > ads.size() ?
                filter.getOffset() + (ads.size() % filter.getLimit()) :
                filter.getOffset() + filter.getLimit();

        List<Ad> paginatedAds = ads.subList(
                filter.getOffset(),
                lastAdIndex
        );
        logger.info("paginated ads legnth: {}", paginatedAds.size());
        return new AdSearchResponse(paginatedAds, ads.size());
    }


    public AdSearchResponse getAdsByCategory(AdFetchingFilter filter) {
        List<Ad> ads = adDao.getAdsByCategory(filter);
        logger.info("total ads legnth: {}", ads.size());

        int lastAdIndex = filter.getOffset() + filter.getLimit() > ads.size() ?
                filter.getOffset() + (ads.size() % filter.getLimit()) :
                filter.getOffset() + filter.getLimit();

        List<Ad> paginatedAds = ads.subList(
                filter.getOffset(),
                lastAdIndex
        );
        logger.info("paginated ads legnth: {}", paginatedAds.size());
        return new AdSearchResponse(paginatedAds, ads.size());
    }



    @Transactional
    public void postAd(CreateAdDto createAdDto) throws IOException, UserException {
        User user = userDao.findUserByUsername(createAdDto.getUsername());
        if (user == null) {
            logger.error("Error posting ad: User {} not found", createAdDto.getUsername());
            throw new UserException("User not found");
        }
        List<Image> images = new ArrayList<>();


        if (createAdDto.getImages() != null) {
            for (MultipartFile imageFile : createAdDto.getImages()) {
                String imageUrl = s3Service.uploadFile(imageFile);
                images.add(new Image(imageUrl));
            }

            adDao.saveImages(images);
        }

        LocalDateTime creationTime = LocalDateTime.now();
        Category category = categoryDao.getCategory(createAdDto.getCategoryId());

        Ad ad = new Ad(
                createAdDto.getTitle(),
                createAdDto.getPrice(),
                createAdDto.getDescription(),
                createAdDto.getCondition(),
                creationTime,
                category,
                creationTime,
                user,
                images,
                false
        );

        adDao.postAd(ad);
    }

    @Transactional
    public void updateUserAdViews(User user, UUID adId) {
        Ad ad = adDao.getAd(adId);

        boolean hasSeenAd = adDao.checkIfUserSeen(user.getId(), adId);
        if (!hasSeenAd) {
            adDao.saveAdView(new UserActivity(user, ad, null));
            adDao.updateViewsCount(adId);
        }
    }


    @Transactional
    public void updateGuestAdViews(String ipAddress, UUID adId) {
        Ad ad = adDao.getAd(adId);

        boolean hasSeenAd = adDao.checkIfGuestSeen(ipAddress, adId);
        if (!hasSeenAd) {
            adDao.saveAdView(new UserActivity(null, ad, ipAddress));
            adDao.updateViewsCount(adId);
        }
    }



}
