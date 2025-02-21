package org.example.app.Services.AdsFetching;

import org.example.app.Daos.AdDao;
import org.example.app.Models.Entities.Ad;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PersonalizedAdsStrat implements AdFetchStrategy {
    private final AdDao adDao;

    public PersonalizedAdsStrat(AdDao adDao) {
        this.adDao = adDao;
    }

    @Override
    public List<Ad> fetchAds(AdFetchingFilter filter) {
        List<Ad> ads = adDao.getPersonalizedAds(filter);
//        Collections.shuffle(ads);
        return ads.subList(0, Math.min(ads.size(), filter.getLimit()));
    }
}
