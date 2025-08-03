package com.interview.business.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "trail-info-client", url = "${hike-app.trail-info.base-url}",
        configuration = TrailInfoClientInterceptor.class)
public interface TrailInfoClient {

    @GetMapping(value = "/search", produces = "application/json")
    List<TrailInfoResponse> search(@RequestParam("q") String query, @RequestParam("format") String format);

}
