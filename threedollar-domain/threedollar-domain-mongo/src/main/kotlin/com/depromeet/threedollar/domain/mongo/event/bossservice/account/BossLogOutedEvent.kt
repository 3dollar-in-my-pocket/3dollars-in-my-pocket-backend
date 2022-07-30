package com.depromeet.threedollar.domain.mongo.event.bossservice.account

data class BossLogOutedEvent(
    val bossId: String,
) {

    companion object {
        fun of(bossId: String): BossLogOutedEvent {
            return BossLogOutedEvent(
                bossId = bossId
            )
        }
    }
}
