package com.depromeet.threedollar.boss.api.service.account.dto.request

import com.depromeet.threedollar.document.boss.document.account.PushSettingsStatus
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UpdateBossAccountInfoRequest(
    @field:NotBlank(message = "이름을 입력해주세요")
    val name: String = "",

    @field:NotNull(message = "푸시 알림 설정을 선택해주세요")
    val pushSettingsStatus: PushSettingsStatus
)
