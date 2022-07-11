package com.depromeet.threedollar.api.adminservice.service.bossservice.registration.dto.request

import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRejectReasonType

data class RejectBossRegistrationRequest(
    val rejectReason: BossRegistrationRejectReasonType,
)
