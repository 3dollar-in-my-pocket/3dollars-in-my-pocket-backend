package com.depromeet.threedollar.api.bossservice.runner

import com.depromeet.threedollar.common.type.FamousPlace
import com.depromeet.threedollar.infrastructure.external.client.google.GoogleAuthApiClient
import com.depromeet.threedollar.infrastructure.external.client.kakao.KaKaoAuthApiClient
import com.depromeet.threedollar.infrastructure.external.client.local.LocalBossApiWarmupApiClient
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

private const val API_CALL_COUNT = 5

@Profile("dev", "staging", "prod")
@Component
class ApplicationWarmingUpRunner(
    private val bossApiWarmupApiClient: LocalBossApiWarmupApiClient,
    private val kakaoAuthApiClient: KaKaoAuthApiClient,
    private val googleAuthApiClient: GoogleAuthApiClient,
) {

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    fun warmingUp() {
        try {
            for (i in 0 until API_CALL_COUNT) {
                for (place in FamousPlace.values()) {
                    bossApiWarmupApiClient.retrieveNearBossStores(place.latitude, place.longitude, 2.0)
                }
                bossApiWarmupApiClient.retrieveBossStoreCategories()
                bossApiWarmupApiClient.retrieveFaqs()
                kakaoAuthApiClient.getProfileInfo("Dummy Kakao Auth Token")
                googleAuthApiClient.getProfileInfo("Dummy Google Auth Token")
            }
        } catch (ignored: Exception) {
        }
    }

}
