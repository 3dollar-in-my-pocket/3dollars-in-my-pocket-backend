package com.depromeet.threedollar.api.admin.controller.user.medal

import com.depromeet.threedollar.api.admin.config.interceptor.Auth
import com.depromeet.threedollar.api.admin.service.user.medal.AdminMedalService
import com.depromeet.threedollar.api.admin.service.user.medal.dto.request.AddMedalRequest
import com.depromeet.threedollar.api.admin.service.user.medal.dto.request.UpdateMedalRequest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.user.medal.MedalResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class AdminMedalController(
    private val adminMedalService: AdminMedalService
) {

    @ApiOperation("새로운 메달을 추가합니다")
    @Auth
    @PostMapping("/v1/user/medal")
    fun addMedal(
        @Valid @RequestBody request: AddMedalRequest
    ): ApiResponse<String> {
        adminMedalService.addMedal(request)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("특정 메달의 정보를 수정합니다")
    @Auth
    @PutMapping("/v1/user/medal/{medalId}")
    fun updateMedal(
        @PathVariable medalId: Long,
        @Valid @RequestBody request: UpdateMedalRequest
    ): ApiResponse<String> {
        adminMedalService.updateMedal(medalId, request)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("전체 메달 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/user/medals")
    fun retrieveMedals(): ApiResponse<List<MedalResponse>> {
        return ApiResponse.success(adminMedalService.retrieveMedals())
    }

}
