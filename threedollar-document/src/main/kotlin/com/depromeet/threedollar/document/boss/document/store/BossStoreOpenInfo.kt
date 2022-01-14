package com.depromeet.threedollar.document.boss.document.store

import java.time.LocalDateTime

data class BossStoreOpenInfo(
    val isOpen: BossStoreOpenType = BossStoreOpenType.IN_ACTIVE,
    val lastUpdateDateTime: LocalDateTime?
)
