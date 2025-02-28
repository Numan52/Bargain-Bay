package org.example.app.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.app.Daos.UserDao;
import org.example.app.Exceptions.ExceptionUtil;
import org.example.app.Exceptions.ParameterException;
import org.example.app.Exceptions.UserException;
import org.example.app.Models.Dtos.AdDto;
import org.example.app.Models.Dtos.AdSearchResponse;
import org.example.app.Models.Dtos.CreateAdDto;
import org.example.app.Models.Entities.Ad;
import org.example.app.Models.Entities.User;
import org.example.app.Security.JwtUtil;
import org.example.app.Services.AdService;
import org.example.app.Services.AdsFetching.*;
import org.example.app.Services.UserService;
import org.example.app.Services.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
public class AdController {
    private final UserDao userDao;
    private final UserService userService;

    private AdService adService;
    private JwtUtil jwtUtil;
    private final static Logger logger = LoggerFactory.getLogger(AdController.class);
    private final TrendingAdsStrat trendingAdsStrat;
    private final FreshAdsStrat freshAdsStrat;
    private final PersonalizedAdsStrat personalizedAdsStrat;


    public AdController(
            AdService adService, JwtUtil jwtUtil, UserDao userDao, UserService userService,
            TrendingAdsStrat trendingAdsStrat, FreshAdsStrat freshAdsStrat, PersonalizedAdsStrat personalizedAdsStrat) {
        this.adService = adService;
        this.jwtUtil = jwtUtil;
        this.userDao = userDao;
        this.userService = userService;
        this.trendingAdsStrat = trendingAdsStrat;
        this.freshAdsStrat = freshAdsStrat;
        this.personalizedAdsStrat = personalizedAdsStrat;
    }


    @GetMapping("/ad")
    public ResponseEntity<?> getAd(@RequestParam String id) throws Exception {
        try {
            Ad ad = adService.getAd(UUID.fromString(id));
            if (ad == null) {
                return ResponseEntity.status(404).body(ExceptionUtil.buildErrorResponse(HttpStatus.valueOf(404), "Ad not found", "/ad"));
            }
            AdDto adDto = Utils.toAdDto(ad);
            return ResponseEntity.ok(adDto);
        } catch (Exception e) {
            logger.error("error getting ad: ", e);
            throw new Exception("An unexpected error occurred");
        }
    }


    @GetMapping("/ads")
    public ResponseEntity<?> getAds(@RequestParam int offset, @RequestParam int limit) throws Exception {
        try {
            AdFetchingFilter filter = new AdFetchingFilter.Builder()
                    .limit(limit)
                    .offset(offset)
                    .build();

            AdSearchResponse response = adService.getAllAds(filter);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("error getting ads: ", e);
            throw new Exception("An unexpected error occurred.");
        }
    }


    @PostMapping(path = "/ads", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> postAd(@RequestPart("ad") CreateAdDto createAdDto,
                                    @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                    HttpServletRequest request) throws Exception {
        logger.info("posting ad");

        try {
            String username = (String) request.getAttribute("username");
            createAdDto.setUsername(username);
            createAdDto.setImages(images);
            logger.info("created ad dto: {}", createAdDto);
            adService.postAd(createAdDto);
        } catch (UserException e) {
            logger.error("Error posting ad: ", e);
            return ResponseEntity.status(404).body(ExceptionUtil.buildErrorResponse(HttpStatus.valueOf(404), e.getMessage(), "/ads"));
        }

        return ResponseEntity.ok().build();
    }


    // TODO: Why so so many sql queries in console
    @GetMapping("/ads/trending")
    public ResponseEntity<?> getTrendingAds(@RequestParam(required = false) Integer limit) throws Exception {
        if (limit == null || limit <= 0) {
            throw new ParameterException("missing or invalid limit parameter");
        }

        AdFetchingFilter filter = new AdFetchingFilter.Builder()
                .limit(limit)
                .build();

        List<Ad> ads = adService.getAds(trendingAdsStrat, filter);
        List<AdDto> adDtos = Utils.toAdDtos(ads);

        return ResponseEntity.ok(adDtos);

    }


    @GetMapping("/ads/fresh")
    public ResponseEntity<?> getFreshAds(Integer limit) throws Exception {
        if (limit == null || limit <= 0) {
            throw new ParameterException("missing or invalid limit parameter");
        }

        AdFetchingFilter filter = new AdFetchingFilter.Builder()
                .limit(limit)
                .build();

        List<Ad> ads = adService.getAds(freshAdsStrat, filter);
        List<AdDto> adDtos = Utils.toAdDtos(ads);

        return ResponseEntity.ok(adDtos);
    }


    @GetMapping("/ads/personalized-user")
    public ResponseEntity<?> getPersonalizedAds(Integer limit, HttpServletRequest request) throws Exception {
        if (limit == null || limit <= 0) {
            throw new ParameterException("missing or invalid limit parameter");
        }
        String username = (String) request.getAttribute("username");
        User user = userService.findUserByName(username);
        logger.info("limit: {}", limit);
        AdFetchingFilter filter = new AdFetchingFilter.Builder()
                .limit(limit)
                .userId(user.getId())
                .build();

        List<Ad> ads = adService.getAds(personalizedAdsStrat, filter);
        logger.info("personalized ads length: {}", ads.size());
        List<AdDto> adDtos = Utils.toAdDtos(ads);

        return ResponseEntity.ok(adDtos);

    }


    @GetMapping("/ads/filtered-by-query")
    public ResponseEntity<?> getAdsByQuery(String searchText, Integer offset, Integer limit) throws Exception {
        if (searchText == null || searchText.isBlank() || offset == null || limit == null) {
            throw new ParameterException("Missing or invalid Parameters. Provide the following Parameters: " + "<searchText>, <offset>, <limit>");
        }

        AdFetchingFilter filter = new AdFetchingFilter.Builder()
                .limit(limit)
                .offset(offset)
                .query(searchText)
                .build();

        AdSearchResponse response = adService.getSearchedAds(filter);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/ads/filtered-by-category")
    public ResponseEntity<?> getAdsByCategory(UUID categoryId, Integer offset, Integer limit) throws Exception {
        if (categoryId == null || offset == null || limit == null) {
            throw new ParameterException("Missing or invalid Parameters. Provide the following Parameters: " + "<searchText>, <offset>, <limit>");
        }

        AdFetchingFilter filter = new AdFetchingFilter.Builder()
                .limit(limit)
                .offset(offset)
                .category(categoryId)
                .build();

        AdSearchResponse response = adService.getAdsByCategory(filter);

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/ads/{adId}/user-views")
    public ResponseEntity<?> updateUserAdViews(@PathVariable UUID adId, HttpServletRequest request) throws Exception {
        String username = (String) request.getAttribute("username");
        User user = userService.findUserByName(username);
        adService.updateUserAdViews(user, adId);

        return ResponseEntity.ok().build();

    }


    @PatchMapping("/ads/{adId}/guest-views")
    public ResponseEntity<?> updateGuestAdViews(@PathVariable UUID adId, HttpServletRequest request) throws Exception {
        String ipAddress = request.getRemoteAddr();

        adService.updateGuestAdViews(ipAddress, adId);

        return ResponseEntity.ok().build();

    }


}
