package com.depromeet.threedollar.api.user.runner;

import com.depromeet.threedollar.common.type.FamousPlace;
import com.depromeet.threedollar.domain.rds.user.domain.advertisement.AdvertisementPlatformType;
import com.depromeet.threedollar.external.client.local.LocalUserApiWarmUpApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile({"dev", "stage", "prod"})
@RequiredArgsConstructor
@Component
public class ApplicationWarmingUpRunner implements ApplicationListener<ApplicationReadyEvent> {

    private static final int API_CALL_COUNT = 20;

    private final LocalUserApiWarmUpApiClient apiClient;

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        try {
            Thread.sleep(10_000);
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
        } catch (InterruptedException exception) {
            log.error(exception.getMessage(), exception);
        } catch (Exception ignored) {
        }
    }

}
