package com.depromeet.threedollar.api.boss.listener.store

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import com.depromeet.threedollar.api.boss.service.store.BossStoreService
import com.depromeet.threedollar.domain.mongo.foodtruck.event.registration.BossSignOutEvent

@Component
class BossStoreEventListener(
    private val bossStoreService: BossStoreService,
) {

    @Async
    @EventListener
    fun deleteBossStoreByBossId(event: BossSignOutEvent) {
        bossStoreService.deleteBossStoreByBossId(event.bossId)
    }

}
