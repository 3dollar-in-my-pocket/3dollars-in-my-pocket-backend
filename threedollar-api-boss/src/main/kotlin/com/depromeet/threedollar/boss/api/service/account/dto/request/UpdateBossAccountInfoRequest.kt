package com.depromeet.threedollar.boss.api.service.account.dto.request

import com.depromeet.threedollar.document.boss.document.account.PushSettingsStatus

data class UpdateBossAccountInfoRequest(
    val name: String,
    val pushSettingsStatus: PushSettingsStatus
)
