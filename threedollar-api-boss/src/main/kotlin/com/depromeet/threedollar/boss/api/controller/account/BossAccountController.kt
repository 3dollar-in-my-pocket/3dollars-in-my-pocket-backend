package com.depromeet.threedollar.boss.api.controller.account

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.config.resolver.Auth
import com.depromeet.threedollar.boss.api.config.resolver.BossId
import com.depromeet.threedollar.boss.api.service.account.BossAccountService
import com.depromeet.threedollar.boss.api.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.boss.api.service.account.dto.response.BossAccountInfoResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class BossAccountController(
    private val bossAccountService: BossAccountService
) {

    @ApiOperation("[인증] 사장님 자신의 회원 정보를 조회합니다")
    @Auth
    @GetMapping("/v1/boss-account/me")
    fun getMyAccountInfo(
        @BossId bossId: String
    ): ApiResponse<BossAccountInfoResponse> {
        return ApiResponse.success(bossAccountService.getBossAccountInfo(bossId))
    }

    @ApiOperation("[인증] 사장님 자신의 회원 정보를 수정합니다")
    @Auth
    @PutMapping("/v1/boss-account/me")
    fun updateMyAccountInfo(
        @BossId bossId: String,
        @Valid @RequestBody request: UpdateBossAccountInfoRequest
    ): ApiResponse<String> {
        bossAccountService.updateBossAccountInfo(bossId, request)
        return ApiResponse.SUCCESS
    }

}
