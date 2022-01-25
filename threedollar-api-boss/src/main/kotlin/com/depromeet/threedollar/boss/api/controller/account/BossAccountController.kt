package com.depromeet.threedollar.boss.api.controller.account

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.config.resolver.Auth
import com.depromeet.threedollar.boss.api.config.resolver.BossId
import com.depromeet.threedollar.boss.api.service.account.BossAccountService
import com.depromeet.threedollar.boss.api.service.account.dto.response.BossAccountInfoResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BossAccountController(
    private val bossAccountService: BossAccountService
) {

    @ApiOperation("[인증] 사장님 자신의 계정 정보를 조회합니다")
    @Auth
    @GetMapping("/boss/v1/account/me")
    fun getMyAccountInfo(
        @BossId bossId: String
    ): ApiResponse<BossAccountInfoResponse> {
        return ApiResponse.success(bossAccountService.getBossAccountInfo(bossId))
    }

}
