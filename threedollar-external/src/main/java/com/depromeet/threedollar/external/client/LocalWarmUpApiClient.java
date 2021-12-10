package com.depromeet.threedollar.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "localWarmUpApiClient", url = "http://localhost:5000")
public interface LocalWarmUpApiClient {

    @GetMapping("/api/v2/stores/near")
    void retrieveNearStores(
        @RequestParam double latitude,
        @RequestParam double longitude,
        @RequestParam double mapLatitude,
        @RequestParam double mapLongitude,
        @RequestParam int distance
    );

    @GetMapping("/api/v2/faqs")
    void getFaqs();

    @GetMapping("/api/v1/popups")
    void getPopups(@RequestParam String platform);

}
