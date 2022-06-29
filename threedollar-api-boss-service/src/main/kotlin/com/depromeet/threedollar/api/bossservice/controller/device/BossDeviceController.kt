package com.depromeet.threedollar.api.bossservice.controller.device

import javax.validation.Valid
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.bossservice.config.interceptor.Auth
import com.depromeet.threedollar.api.bossservice.config.resolver.BossId
import com.depromeet.threedollar.api.bossservice.controller.device.dto.request.UpsertBossDeviceRequest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.commonservice.device.DeviceService
import io.swagger.annotations.ApiOperation

@RestController
class BossDeviceController(
    private val deviceService: DeviceService,
) {

    @ApiOperation("사장님 계정의 디바이스 정보를 저장 및 갱신합니다")
    @Auth
    @PutMapping("/v1/device")
    fun upsertDevice(
        @Valid @RequestBody request: UpsertBossDeviceRequest,
        @BossId bossId: String,
    ): ApiResponse<String> {
        deviceService.upsertDeviceAsync(request = request.toUpsertDeviceRequest(accountId = bossId))
        return ApiResponse.OK
    }

}
