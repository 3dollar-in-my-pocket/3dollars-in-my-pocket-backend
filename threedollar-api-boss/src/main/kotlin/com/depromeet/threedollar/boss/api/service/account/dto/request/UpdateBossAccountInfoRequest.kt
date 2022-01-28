package com.depromeet.threedollar.boss.api.service.account.dto.request

import javax.validation.constraints.NotBlank

data class UpdateBossAccountInfoRequest(
    @field:NotBlank(message = "이름을 입력해주세요")
    val name: String = ""
)
