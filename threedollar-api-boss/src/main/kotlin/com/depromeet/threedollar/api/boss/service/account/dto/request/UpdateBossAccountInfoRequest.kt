package com.depromeet.threedollar.api.boss.service.account.dto.request

import javax.validation.constraints.Size

data class UpdateBossAccountInfoRequest(
    @field:Size(max = 30, message = "{account.name.size}")
    val name: String,

    val isSetupNotification: Boolean
)
