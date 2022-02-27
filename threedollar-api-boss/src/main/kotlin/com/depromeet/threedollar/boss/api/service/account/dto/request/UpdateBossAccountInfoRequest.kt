package com.depromeet.threedollar.boss.api.service.account.dto.request

import com.depromeet.threedollar.document.boss.document.account.PushSettingsStatus
import javax.validation.constraints.NotBlank

data class UpdateBossAccountInfoRequest(
    @field:NotBlank(message = "{account.name.notBlank}")
    val name: String = "",
    val pushSettingsStatus: PushSettingsStatus
)
