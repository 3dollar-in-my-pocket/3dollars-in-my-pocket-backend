package com.depromeet.threedollar.boss.api.listener.store

import com.depromeet.threedollar.boss.api.service.store.BossStoreService
import com.depromeet.threedollar.document.boss.event.registration.BossSignOutEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class BossStoreEventListener(
    private val bossStoreService: BossStoreService
) {

    @Async
    @EventListener
    fun deleteBossStoreByBossId(event: BossSignOutEvent) {
        bossStoreService.deleteBossStoreByBossId(event.bossId)
    }

}
