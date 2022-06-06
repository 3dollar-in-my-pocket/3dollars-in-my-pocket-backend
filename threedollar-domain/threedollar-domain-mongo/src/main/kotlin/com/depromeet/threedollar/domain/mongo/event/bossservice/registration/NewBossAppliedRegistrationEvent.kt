package com.depromeet.threedollar.domain.mongo.event.bossservice.registration

import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistration

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
