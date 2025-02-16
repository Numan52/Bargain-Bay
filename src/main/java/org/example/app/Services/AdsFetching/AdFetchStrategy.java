package org.example.app.Services.AdsFetching;

import org.example.app.Models.Entities.Ad;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AdFetchStrategy {

    List<Ad> fetchAds(int limit);
}
