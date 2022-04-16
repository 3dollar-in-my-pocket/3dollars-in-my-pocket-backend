package com.depromeet.threedollar.external.client.local;

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
        @RequestParam double mapLatitude,
        @RequestParam double mapLongitude,
        @RequestParam int distanceKm
    );

    @GetMapping("/v1/boss/store/categories")
    void retrieveBossStoreCategories();

}
