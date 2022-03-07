package com.depromeet.threedollar.api.boss.service.account.dto.request

import org.hibernate.validator.constraints.Length

data class UpdateBossAccountInfoRequest(
    @field:Length(max = 30, message = "{account.name.length}")
    val name: String,

    val pushSettingsStatus: com.depromeet.threedollar.domain.mongo.boss.domain.account.PushSettingsStatus
)
