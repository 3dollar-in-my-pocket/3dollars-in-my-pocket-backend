package com.depromeet.threedollar.boss.api.service.account.dto.request

import com.depromeet.threedollar.document.boss.document.account.PushSettingsStatus
import org.hibernate.validator.constraints.Length

data class UpdateBossAccountInfoRequest(
    @field:Length(max = 30, message = "{account.name.length}")
    val name: String,

    val pushSettingsStatus: PushSettingsStatus
)
