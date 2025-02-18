package org.example.app.Services.AdsFetching;

import org.example.app.Daos.AdDao;
import org.example.app.Models.Entities.Ad;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TrendingAdsStrat implements AdFetchStrategy{
    private final AdDao adDao;

    public TrendingAdsStrat(AdDao adDao) {
        this.adDao = adDao;
    }

    @Override
    public List<Ad> fetchAds(AdFetchingFilter filter) {
        List<Ad> ads = adDao.getTrendingAds(40);
        Collections.shuffle(ads);
        return ads.subList(0, Math.min(ads.size(), filter.getLimit()));
    }
}
