package com.depromeet.threedollar.domain.mongo.foodtruck.event.registration

import com.depromeet.threedollar.domain.mongo.foodtruck.domain.registration.BossRegistration

data class NewBossAppliedRegistrationEvent(
    val bossRegistration: BossRegistration,
) {

    companion object {
        fun of(bossRegistration: BossRegistration): NewBossAppliedRegistrationEvent {
            return NewBossAppliedRegistrationEvent(
                bossRegistration = bossRegistration
            )
        }
    }

}
