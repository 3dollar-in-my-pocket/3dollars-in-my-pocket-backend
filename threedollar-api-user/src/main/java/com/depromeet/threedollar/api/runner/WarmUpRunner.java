package com.depromeet.threedollar.api.runner;

import com.depromeet.threedollar.common.type.FamousPlace;
import com.depromeet.threedollar.domain.user.domain.popup.PopupPlatformType;
import com.depromeet.threedollar.external.client.local.LocalWarmUpApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"dev", "stage", "prod"})
@RequiredArgsConstructor
@Component
public class WarmUpRunner implements CommandLineRunner {

    private static final int API_CALL_COUNT = 10;

    private final LocalWarmUpApiClient apiClient;

    @Override
    public void run(String... args) {
        try {
            for (int i = 0; i < API_CALL_COUNT; i++) {
                for (FamousPlace place : FamousPlace.values()) {
                    apiClient.retrieveNearStores(place.getLatitude(), place.getLongitude(), place.getLatitude(), place.getLongitude(), 2000);
                }
                apiClient.getMedals();
                apiClient.getStoreMenuCategories();
                apiClient.getFaqs();
                for (PopupPlatformType platformType : PopupPlatformType.values()) {
                    apiClient.getPopups(platformType.name());
                }
            }
        } catch (Exception ignored) {
        }
    }

}
