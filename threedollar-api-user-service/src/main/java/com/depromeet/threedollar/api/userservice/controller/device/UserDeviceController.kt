package com.depromeet.threedollar.api.userservice.controller.device

import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.commonservice.device.DeviceService
import com.depromeet.threedollar.api.userservice.config.interceptor.Auth
import com.depromeet.threedollar.api.userservice.config.resolver.UserId
import com.depromeet.threedollar.api.userservice.controller.device.dto.request.UpsertUserDeviceRequest
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class UserDeviceController(
    private val deviceService: DeviceService,
) {

    @ApiOperation("사장님 계정의 디바이스 정보를 저장 및 갱신합니다")
    @Auth
    @PutMapping("/v1/device")
    fun upsertDevice(
        @Valid @RequestBody request: UpsertUserDeviceRequest,
        @UserId userId: Long?,
    ): ApiResponse<String> {
        deviceService.upsertDeviceAsync(request = request.toUpsertDeviceRequest(accountId = userId ?: -1L))
        return ApiResponse.OK
    }

}
