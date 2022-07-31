package com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.response

import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreOpenType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpen
import java.time.LocalDateTime

data class BossStoreOpenStatusResponse(
    val status: BossStoreOpenType,
    val openStartDateTime: LocalDateTime?,
) {

    companion object {
        fun open(bossStoreOpen: BossStoreOpen): BossStoreOpenStatusResponse {
            return BossStoreOpenStatusResponse(
                status = BossStoreOpenType.OPEN,
                openStartDateTime = bossStoreOpen.openStartDateTime
            )
        }

        fun close(): BossStoreOpenStatusResponse {
            return BossStoreOpenStatusResponse(
                status = BossStoreOpenType.CLOSED,
                openStartDateTime = null
            )
        }
    }

}
