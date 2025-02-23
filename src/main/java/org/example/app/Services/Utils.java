package org.example.app.Services;

import org.example.app.Models.Dtos.AdDto;
import org.example.app.Models.Entities.Ad;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static AdDto toAdDto(Ad ad) {
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


    public static List<AdDto> toAdDtos(List<Ad> ads) {
        List<AdDto> adDtos = new ArrayList<>();
        for (Ad ad : ads) {
            adDtos.add(toAdDto(ad));
        }
        return adDtos;
    }
}
