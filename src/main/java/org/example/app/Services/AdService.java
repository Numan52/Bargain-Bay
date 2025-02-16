package org.example.app.Services;

import jakarta.transaction.Transactional;
import org.example.app.Daos.AdDao;
import org.example.app.Daos.UserDao;
import org.example.app.Exceptions.UserException;
import org.example.app.Models.Dtos.AdDto;
import org.example.app.Models.Dtos.CreateAdDto;
import org.example.app.Models.Entities.Ad;
import org.example.app.Models.Entities.AdView;
import org.example.app.Models.Entities.Image;
import org.example.app.Models.Entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdService {
    private AdDao adDao;
    private UserDao userDao;
    private S3Service s3Service;


    private static final Logger logger = LoggerFactory.getLogger(AdService.class);


    public AdService(AdDao adDao, UserDao userDao, S3Service s3Service) {
        this.adDao = adDao;
        this.userDao = userDao;
        this.s3Service = s3Service;

    }


     @Transactional
     public Ad getAd(UUID id) {
        return adDao.getAd(id);
     }


    @Transactional
    public List<Ad> getAds(int offset, int limit) {
        List<Ad> ads = adDao.getAds(offset, limit);
        return ads;
    }

    @Transactional
    public List<Ad> getTrendingAds(int limit) {
        List<Ad> ads = adDao.getTrendingAds(40);
        Collections.shuffle(ads);
        return ads.subList(0, Math.min(ads.size(), limit));
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

        Ad ad = new Ad(
                createAdDto.getTitle(),
                createAdDto.getPrice(),
                createAdDto.getDescription(),
                createAdDto.getCondition(),
                creationTime,
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
            adDao.saveAdView(new AdView(user, ad, null));
            adDao.updateViewsCount(adId);
        }
    }


    @Transactional
    public void updateGuestAdViews(String ipAddress, UUID adId) {
        Ad ad = adDao.getAd(adId);

        boolean hasSeenAd = adDao.checkIfGuestSeen(ipAddress, adId);
        if (!hasSeenAd) {
            adDao.saveAdView(new AdView(null, ad, ipAddress));
            adDao.updateViewsCount(adId);
        }
    }


    public AdDto toDto(Ad ad) {
        return new AdDto(
                ad.getId(),
                ad.getTitle(),
                ad.getPrice(),
                ad.getDescription(),
                ad.getCondition(),
                ad.getUser().getId(),
                ad.getImages().stream().map((image -> image.getUrl())).collect(Collectors.toList()),
                ad.isHasPriority(),
                ad.getCreatedAt(),
                ad.getLastBumpedAt()
        );

    }


    public List<AdDto> toDtos(List<Ad> ads) {
        List<AdDto> adDtos = new ArrayList<>();
        for (Ad ad : ads) {
            adDtos.add(toDto(ad));
        }
        return adDtos;
    }

}
