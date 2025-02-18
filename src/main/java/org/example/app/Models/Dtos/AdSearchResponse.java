package org.example.app.Models.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.app.Models.Entities.Ad;
import org.example.app.Services.Utils;

import java.util.List;

@Setter
@Getter

public class AdSearchResponse {
    private List<AdDto> ads;
    private int totalAds;

    public AdSearchResponse(List<Ad> ads, int totalAds) {
        this.ads = Utils.toDtos(ads);
        this.totalAds = totalAds;
    }
}
