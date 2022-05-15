package com.depromeet.threedollar.api.user.runner;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.depromeet.threedollar.common.type.FamousPlace;
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPlatformType;
import com.depromeet.threedollar.external.client.local.LocalUserApiWarmUpApiClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({"dev", "staging", "prod"})
@RequiredArgsConstructor
@Component
public class ApplicationWarmingUpRunner {

    private static final int API_CALL_COUNT = 10;

    private final LocalUserApiWarmUpApiClient apiClient;

    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    public void warmingUp() {
        try {
            for (int i = 0; i < API_CALL_COUNT; i++) {
                for (FamousPlace place : FamousPlace.values()) {
                    apiClient.retrieveNearUserStores(place.getLatitude(), place.getLongitude(), place.getLatitude(), place.getLongitude(), 2000);
                }
                apiClient.getMedals();
                apiClient.getStoreMenuCategories();
                apiClient.getFaqs();
                for (AdvertisementPlatformType platformType : AdvertisementPlatformType.values()) {
                    apiClient.getAdvertisements(platformType.name());
                }
            }
        } catch (Exception ignored) {
        }
    }

}
