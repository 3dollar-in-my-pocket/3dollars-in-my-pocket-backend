package com.depromeet.threedollar.infrastructure.external.client.local;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "localWarmUpUserApiClient",
    url = "http://localhost:${server.port}/api"
)
public interface LocalUserApiWarmUpApiClient {

    @GetMapping("/v2/stores/near")
    void retrieveNearUserStores(
        @RequestParam("latitude") double latitude,
        @RequestParam("longitude") double longitude,
        @RequestParam("mapLatitude") double mapLatitude,
        @RequestParam("mapLongitude") double mapLongitude,
        @RequestParam("distance") int distance
    );

    @GetMapping("/v2/faqs")
    void getFaqs();

    @GetMapping("/v1/advertisements")
    void getAdvertisements(@RequestParam("platform") String platform);

    @GetMapping("/v1/medals")
    void getMedals();

    @GetMapping("/v2/store/menu/categories")
    void getStoreMenuCategories();

    @GetMapping("/v1/boss/stores/around")
    void retrieveNearBossStores(
        @RequestParam("mapLatitude") double mapLatitude,
        @RequestParam("mapLongitude") double mapLongitude,
        @RequestParam("distanceKm") double distanceKm
    );

    @GetMapping("/v1/boss/store/categories")
    void retrieveBossStoreCategories();

}
