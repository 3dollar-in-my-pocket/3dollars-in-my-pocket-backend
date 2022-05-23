package com.depromeet.threedollar.external.client.local;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "localWarmUpBossApiClient",
    url = "http://localhost:${server.port}/boss"
)
public interface LocalBossApiWarmupApiClient {

    @Retryable(maxAttempts = 5, backoff = @Backoff(value = 1000), value = Exception.class)
    @GetMapping("/v1/boss/stores/around")
    void retrieveNearBossStores(
        @RequestParam("mapLatitude") double mapLatitude,
        @RequestParam("mapLongitude") double mapLongitude,
        @RequestParam("distanceKm") double distanceKm
    );

    @Retryable(maxAttempts = 5, backoff = @Backoff(value = 1000), value = Exception.class)
    @GetMapping("/v1/boss/store/categories")
    void retrieveBossStoreCategories();

}
