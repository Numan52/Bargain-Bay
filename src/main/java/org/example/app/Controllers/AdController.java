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


    @PostMapping(path = "/ads", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> postAd(@RequestPart("ad") CreateAdDto createAdDto,
                                    @RequestPart("images") List<MultipartFile> images,
                                    HttpServletRequest request) {
        logger.info("posting ad");
        try {
            String username = (String) request.getAttribute("username");
            createAdDto.setUsername(username);
            createAdDto.setImages(images);
            logger.info("created ad dto: {}", createAdDto);
            adService.postAd(createAdDto);
        } catch (Exception e) {
            logger.error("Error posting ad: ", e);
            return ExceptionUtil.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "/ads");
        }

        return ResponseEntity.ok().build();
    }
}
