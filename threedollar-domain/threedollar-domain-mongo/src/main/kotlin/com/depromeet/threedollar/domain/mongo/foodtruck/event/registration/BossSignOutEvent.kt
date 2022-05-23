package com.depromeet.threedollar.domain.mongo.foodtruck.event.registration

data class BossSignOutEvent(
    val bossId: String,
) {

    companion object {
        fun of(bossId: String): BossSignOutEvent {
            return BossSignOutEvent(
                bossId = bossId
            )
        }
    }

}
