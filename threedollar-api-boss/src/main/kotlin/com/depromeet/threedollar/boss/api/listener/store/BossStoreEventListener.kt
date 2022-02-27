package com.depromeet.threedollar.boss.api.listener.store

import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import com.depromeet.threedollar.document.boss.event.registration.BossSignOutEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class BossStoreEventListener(
    private val bossStoreRepository: BossStoreRepository
) {

    @Async
    @EventListener
    fun deleteBossStore(event: BossSignOutEvent) {
        bossStoreRepository.findActiveBossStoreByBossId(bossId = event.bossId)?.let {
            it.delete()
            bossStoreRepository.save(it)
        }
    }

}
