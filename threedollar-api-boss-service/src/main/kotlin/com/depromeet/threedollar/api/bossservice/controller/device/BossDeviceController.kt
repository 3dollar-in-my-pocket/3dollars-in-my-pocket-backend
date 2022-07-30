package com.depromeet.threedollar.api.bossservice.controller.device

import com.depromeet.threedollar.api.bossservice.config.interceptor.Auth
import com.depromeet.threedollar.api.bossservice.config.resolver.BossId
import com.depromeet.threedollar.api.bossservice.controller.device.dto.request.UpsertBossDeviceRequest
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.api.core.service.service.commonservice.device.DeviceService
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class BossDeviceController(
    private val deviceService: DeviceService,
) {

    @ApiOperation("사장님 계정의 디바이스 정보를 저장 및 갱신합니다")
    @Auth(allowedWaiting = true)
    @PutMapping("/v1/device")
    fun upsertDevice(
        @Valid @RequestBody request: UpsertBossDeviceRequest,
        @BossId bossId: String,
    ): ApiResponse<String> {
        deviceService.upsertDevice(request = request.toUpsertDeviceRequest(accountId = bossId))
        return ApiResponse.OK
    }

    @ApiOperation("사장님 계정의 디바이스 정보를 삭제합니다")
    @Auth(allowedWaiting = true)
    @DeleteMapping("/v1/device")
    fun deviceDevice(
        @BossId bossId: String,
    ): ApiResponse<String> {
        deviceService.deleteDevice(accountId = bossId, accountType = AccountType.BOSS_ACCOUNT)
        return ApiResponse.OK
    }

}
