package com.depromeet.threedollar.external.client.local;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "localWarmUpUserApiClient",
    url = "http://localhost:${server.port}/api"
)
public interface LocalUserApiWarmUpApiClient {

    @Retryable(maxAttempts = 5, backoff = @Backoff(value = 1000), value = Exception.class)
    @GetMapping("/v2/stores/near")
    void retrieveNearUserStores(
        @RequestParam("latitude") double latitude,
        @RequestParam("longitude") double longitude,
        @RequestParam("mapLatitude") double mapLatitude,
        @RequestParam("mapLongitude") double mapLongitude,
        @RequestParam("distance") int distance
    );

    @Retryable(maxAttempts = 5, backoff = @Backoff(value = 1000), value = Exception.class)
    @GetMapping("/v2/faqs")
    void getFaqs();

    @Retryable(maxAttempts = 5, backoff = @Backoff(value = 1000), value = Exception.class)
    @GetMapping("/v1/advertisements")
    void getAdvertisements(@RequestParam("platform") String platform);

    @Retryable(maxAttempts = 5, backoff = @Backoff(value = 1000), value = Exception.class)
    @GetMapping("/v1/medals")
    void getMedals();

    @Retryable(maxAttempts = 5, backoff = @Backoff(value = 1000), value = Exception.class)
    @GetMapping("/v2/store/menu/categories")
    void getStoreMenuCategories();

}
