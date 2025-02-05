package org.example.app.Services;

import jakarta.transaction.Transactional;
import org.example.app.Daos.AdDao;
import org.example.app.Daos.UserDao;
import org.example.app.Models.AdDto;
import org.example.app.Models.CreateAdDto;
import org.example.app.Models.Entities.Ad;
import org.example.app.Models.Entities.Image;
import org.example.app.Models.Entities.User;
import org.example.app.Security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public void postAd(CreateAdDto createAdDto) throws IOException {
        User user = userDao.findUserByUsername(createAdDto.getUsername());
        if (user == null) {
            logger.error("Error posting ad: User {} not found", createAdDto.getUsername());
            throw new RuntimeException("User not found");
        }
        List<Image> images = new ArrayList<>();


        if (createAdDto.getImages() != null) {
            for (MultipartFile imageFile : createAdDto.getImages()) {
                String imageUrl = s3Service.uploadFile(imageFile);
                images.add(new Image(imageUrl));
            }

            adDao.saveImages(images);
        }

        Ad ad = new Ad(
                createAdDto.getTitle(),
                createAdDto.getPrice(),
                createAdDto.getDescription(),
                createAdDto.getCondition(),
                user,
                images
        );

        adDao.postAd(ad);
    }
}
