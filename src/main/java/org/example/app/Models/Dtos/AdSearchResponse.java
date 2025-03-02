package org.example.app.Models.Dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.app.Models.Entities.Ad;
import org.example.app.Services.Utils;

import java.util.List;

@Setter
@Getter
@ToString
public class AdSearchResponse {
    private List<AdDto> ads;
    private long totalAds;

    public AdSearchResponse(List<Ad> ads, long totalAds) {
        this.ads = Utils.toAdDtos(ads);
        this.totalAds = totalAds;
    }
}
