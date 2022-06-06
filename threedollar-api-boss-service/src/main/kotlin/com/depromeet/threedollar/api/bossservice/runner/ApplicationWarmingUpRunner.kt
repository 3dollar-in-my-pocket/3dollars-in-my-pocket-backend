package com.depromeet.threedollar.api.bossservice.runner

import java.util.concurrent.TimeUnit
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import com.depromeet.threedollar.common.type.FamousPlace
import com.depromeet.threedollar.external.client.apple.AppleAuthApiClient
import com.depromeet.threedollar.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.external.client.kakao.KaKaoAuthApiClient
import com.depromeet.threedollar.external.client.local.LocalBossApiWarmupApiClient

private const val API_CALL_COUNT = 5

@Profile("dev", "staging", "prod")
@Component
class ApplicationWarmingUpRunner(
    private val apiClient: LocalBossApiWarmupApiClient,
    private val appleAuthApiClient: AppleAuthApiClient,
    private val kakaoAuthApiClient: KaKaoAuthApiClient,
    private val googleAuthApiClient: GoogleAuthApiClient,
) {

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    fun warmingUp() {
        try {
            for (i in 0 until API_CALL_COUNT) {
                for (place in FamousPlace.values()) {
                    apiClient.retrieveNearBossStores(place.latitude, place.longitude, 2.0)
                }
                apiClient.retrieveBossStoreCategories()
                appleAuthApiClient.appleAuthPublicKey
                appleAuthApiClient.appleAuthPublicKey
                kakaoAuthApiClient.getProfileInfo("Dummy Kakao Auth Token")
                googleAuthApiClient.getProfileInfo("Dummy Google Auth Token")
            }
        } catch (ignored: Exception) {
        }
    }

}
