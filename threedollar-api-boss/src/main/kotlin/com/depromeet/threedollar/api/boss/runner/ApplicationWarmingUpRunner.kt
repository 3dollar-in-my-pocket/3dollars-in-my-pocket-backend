package com.depromeet.threedollar.api.boss.runner

import java.util.concurrent.TimeUnit
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import com.depromeet.threedollar.common.type.FamousPlace
import com.depromeet.threedollar.external.client.local.LocalBossApiWarmupApiClient

private const val API_CALL_COUNT = 10

@Profile("dev", "staging", "prod")
@Component
class ApplicationWarmingUpRunner(
    private val apiClient: LocalBossApiWarmupApiClient
) {

    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    fun warmingUp() {
        try {
            for (i in 0 until API_CALL_COUNT) {
                for (place in FamousPlace.values()) {
                    apiClient.retrieveNearBossStores(place.latitude, place.longitude, 3000)
                }
                apiClient.retrieveBossStoreCategories()
            }
        } catch (ignored: Exception) {
        }
    }

}
