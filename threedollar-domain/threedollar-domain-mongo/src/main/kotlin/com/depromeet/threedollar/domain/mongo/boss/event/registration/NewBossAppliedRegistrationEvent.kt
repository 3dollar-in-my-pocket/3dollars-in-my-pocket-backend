package com.depromeet.threedollar.domain.mongo.boss.event.registration

import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistration

data class NewBossAppliedRegistrationEvent(
    val bossRegistration: BossRegistration
) {

    companion object {
        fun of(bossRegistration: BossRegistration): NewBossAppliedRegistrationEvent {
            return NewBossAppliedRegistrationEvent(
                bossRegistration = bossRegistration
            )
        }
    }

}
