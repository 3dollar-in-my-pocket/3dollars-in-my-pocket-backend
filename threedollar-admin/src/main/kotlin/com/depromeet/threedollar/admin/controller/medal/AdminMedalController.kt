package com.depromeet.threedollar.admin.controller.medal

import com.depromeet.threedollar.admin.config.interceptor.Auth
import com.depromeet.threedollar.admin.service.medal.AdminMedalService
import com.depromeet.threedollar.admin.service.medal.dto.request.AddMedalRequest
import com.depromeet.threedollar.admin.service.medal.dto.request.UpdateMedalRequest
import com.depromeet.threedollar.application.common.dto.ApiResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AdminMedalController(
    private val adminMedalService: AdminMedalService
) {

    @ApiOperation("새로운 메달을 추가합니다")
    @Auth
    @PostMapping("/v1/medal")
    fun addMedal(
        @Valid @RequestBody request: AddMedalRequest
    ): ApiResponse<String> {
        adminMedalService.addMedal(request)
        return ApiResponse.SUCCESS
    }

    @ApiOperation("특정 메달의 정보를 수정합니다")
    @Auth
    @PutMapping("/v1/medal/{medalId}")
    fun updateMedal(
        @PathVariable medalId: Long,
        @Valid @RequestBody request: UpdateMedalRequest
    ): ApiResponse<String> {
        adminMedalService.updateMedal(medalId, request)
        return ApiResponse.SUCCESS
    }

}
