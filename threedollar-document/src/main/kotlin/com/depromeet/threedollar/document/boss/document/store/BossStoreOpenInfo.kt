package com.depromeet.threedollar.document.boss.document.store

import java.time.LocalDateTime

data class BossStoreOpenInfo(
    val openStatus: BossStoreOpenType = BossStoreOpenType.CLOSED,
    val firstOpenDateTime: LocalDateTime?, // 오늘 사장님이 처음 개시한 시간 (화면 표시용 시간)
    val lastOpenDateTime: LocalDateTime? // 자동으로 주기적으로 마지막으로 갱신된 시간 (영업 중인지 확인하기 위한 시간)
)
