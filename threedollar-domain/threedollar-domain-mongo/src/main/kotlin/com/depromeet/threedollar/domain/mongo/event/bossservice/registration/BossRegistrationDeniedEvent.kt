package com.depromeet.threedollar.domain.mongo.event.bossservice.registration

import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRejectReasonType

data class BossRegistrationDeniedEvent(
    val bossRegistrationId: String,
    val rejectReasonType: BossRegistrationRejectReasonType,
)
