package com.depromeet.threedollar.api.userservice.runner;

import com.depromeet.threedollar.common.type.FamousPlace;
import com.depromeet.threedollar.domain.rds.domain.commonservice.advertisement.AdvertisementPlatformType;
import com.depromeet.threedollar.infrastructure.external.client.google.GoogleAuthApiClient;
import com.depromeet.threedollar.infrastructure.external.client.kakao.KaKaoAuthApiClient;
import com.depromeet.threedollar.infrastructure.external.client.local.LocalUserApiWarmUpApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Profile({"dev", "staging", "prod"})
@RequiredArgsConstructor
@Component
public class ApplicationWarmingUpRunner {

    private static final int API_CALL_COUNT = 5;

    private final LocalUserApiWarmUpApiClient userApiWarmUpApiClient;
    private final KaKaoAuthApiClient kaKaoAuthApiClient;
    private final GoogleAuthApiClient googleAuthApiClient;

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    public void warmingUp() {
        try {
            for (int i = 0; i < API_CALL_COUNT; i++) {
                for (FamousPlace place : FamousPlace.values()) {
                    userApiWarmUpApiClient.retrieveNearUserStores(place.getLatitude(), place.getLongitude(), place.getLatitude(), place.getLongitude(), 2000);
                    userApiWarmUpApiClient.retrieveNearBossStores(place.getLatitude(), place.getLongitude(), 2.0);
                }
                userApiWarmUpApiClient.getMedals();
                userApiWarmUpApiClient.getStoreMenuCategories();
                userApiWarmUpApiClient.getFaqs();
                for (AdvertisementPlatformType platformType : AdvertisementPlatformType.values()) {
                    userApiWarmUpApiClient.getAdvertisements(platformType.name());
                }
                kaKaoAuthApiClient.getProfileInfo("Dummy Kakao Auth Token");
                googleAuthApiClient.getProfileInfo("Dummy Google Auth Token");

                userApiWarmUpApiClient.retrieveBossStoreCategories();
            }
        } catch (Exception ignored) {
        }
    }

}
