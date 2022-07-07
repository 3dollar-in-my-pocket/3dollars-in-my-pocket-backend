package com.depromeet.threedollar.api.bossservice.listener.store

import com.depromeet.threedollar.api.bossservice.service.store.BossStoreService
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.BossSignOutEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

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
