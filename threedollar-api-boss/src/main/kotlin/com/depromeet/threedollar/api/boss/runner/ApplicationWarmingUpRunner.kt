package com.depromeet.threedollar.api.boss.runner

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import com.depromeet.threedollar.common.type.FamousPlace
import com.depromeet.threedollar.external.client.local.LocalBossApiWarmupApiClient

private const val API_CALL_COUNT = 20

@Profile("dev", "staging", "prod")
@Component
class ApplicationWarmingUpRunner(
    private val apiClient: LocalBossApiWarmupApiClient
) : ApplicationListener<ApplicationReadyEvent> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
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
