package com.depromeet.threedollar.infrastructure.external.client.local;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "localWarmUpBossApiClient",
    url = "http://localhost:${server.port}/boss"
)
public interface LocalBossApiWarmupApiClient {

    @GetMapping("/v1/boss/stores/around")
    void retrieveNearBossStores(
        @RequestParam("mapLatitude") double mapLatitude,
        @RequestParam("mapLongitude") double mapLongitude,
        @RequestParam("distanceKm") double distanceKm
    );

    @GetMapping("/v1/boss/store/categories")
    void retrieveBossStoreCategories();

    @GetMapping("/v1/faqs")
    void retrieveFaqs();

}
