package com.depromeet.threedollar.document.boss.event.registration

import com.depromeet.threedollar.document.boss.document.registration.Registration

data class NewBossAppliedRegistrationEvent(
    val registration: Registration
)
