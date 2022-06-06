package com.depromeet.threedollar.api.bossservice.service.account.dto.request

import com.depromeet.threedollar.api.bossservice.config.validator.BossName

data class UpdateBossAccountInfoRequest(
    @field:BossName
    val name: String,

    val isSetupNotification: Boolean,
)
