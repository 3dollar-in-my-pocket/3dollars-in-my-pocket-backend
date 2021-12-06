package com.depromeet.threedollar.api;

import com.depromeet.threedollar.external.client.LocalWarmUpApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"dev", "prod"})
@RequiredArgsConstructor
@Component
public class WarmingUpApplication implements CommandLineRunner {

    private final LocalWarmUpApiClient apiClient;

    @Override
    public void run(String... args) {
        try {
            for (int i = 0; i < 10; i++) {
                apiClient.retrieveNearStores(34.0, 126.0, 34.0, 126.0, 2000);
                apiClient.getFaqs();
                apiClient.getPopups("AOS");
                apiClient.getPopups("IOS");
            }
        } catch (Exception ignored) {
        }
    }

}
