package com.interview.business;

import com.interview.business.client.TrailInfoClient;
import com.interview.business.client.TrailInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrailAdditionalInfoService {

    private final TrailInfoClient trailInfoClient;

    @Cacheable(value = "trail-coordinates", key = "#locationName")
    public TrailInfoResponse getAdditionalTrailInfo(String locationName) {
        List<TrailInfoResponse> results = trailInfoClient.search(locationName, "json");

        if (!results.isEmpty()) {
            TrailInfoResponse response = results.getFirst();
            return new TrailInfoResponse(response.getLatitude(), response.getLongitude(), response.getPlaceRank());
        }
        return null;
    }
}
