package com.depromeet.threedollar.external.client.local;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "localWarmUpApiClient", url = "http://localhost:5000")
public interface LocalThreedollarsUserApiWarmUpApiClient {

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

    @GetMapping("/api/v1/advertisements")
    void getAdvertisements(@RequestParam String platform);

    @GetMapping("/api/v1/medals")
    void getMedals();

    @GetMapping("/api/v2/store/menu/categories")
    void getStoreMenuCategories();

}
