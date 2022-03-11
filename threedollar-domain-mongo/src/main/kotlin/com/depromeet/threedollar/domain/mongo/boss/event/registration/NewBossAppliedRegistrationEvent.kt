package com.depromeet.threedollar.domain.mongo.boss.event.registration

import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration

data class NewBossAppliedRegistrationEvent(
    val registration: Registration
) {

    companion object {
        fun of(registration: Registration): NewBossAppliedRegistrationEvent {
            return NewBossAppliedRegistrationEvent(registration)
        }
    }

}
