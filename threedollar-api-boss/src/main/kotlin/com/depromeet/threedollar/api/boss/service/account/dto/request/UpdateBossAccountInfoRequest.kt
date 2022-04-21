package com.depromeet.threedollar.api.boss.service.account.dto.request

import com.depromeet.threedollar.api.boss.config.validator.BossName

data class UpdateBossAccountInfoRequest(
    @field:BossName
    val name: String,

    val isSetupNotification: Boolean
)
