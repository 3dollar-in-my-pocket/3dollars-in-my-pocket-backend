package com.depromeet.threedollar.external.client.local;

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
        @RequestParam double latitude,
        @RequestParam double longitude,
        @RequestParam double mapLatitude,
        @RequestParam double mapLongitude,
        @RequestParam int distance
    );

    @GetMapping("/v2/faqs")
    void getFaqs();

    @GetMapping("/v1/advertisements")
    void getAdvertisements(@RequestParam String platform);

    @GetMapping("/v1/medals")
    void getMedals();

    @GetMapping("/v2/store/menu/categories")
    void getStoreMenuCategories();

}
