package org.example.app.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.app.Exceptions.ExceptionUtil;
import org.example.app.Models.AdDto;
import org.example.app.Models.CreateAdDto;
import org.example.app.Models.Entities.User;
import org.example.app.Security.JwtUtil;
import org.example.app.Services.AdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AdController {
    private AdService adService;
    private JwtUtil jwtUtil;
    private final static Logger logger = LoggerFactory.getLogger(AdController.class);


    public AdController(AdService adService, JwtUtil jwtUtil) {
        this.adService = adService;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping("ads")
    public ResponseEntity<?> getAds() {

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
        } catch (Exception e) {
            logger.error("Error posting ad: ", e);
            throw new Exception("An unexpected error occurred.");
        }

        return ResponseEntity.ok().build();
    }



}
