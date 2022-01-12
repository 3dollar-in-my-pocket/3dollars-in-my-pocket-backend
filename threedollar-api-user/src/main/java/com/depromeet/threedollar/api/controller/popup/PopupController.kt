package com.depromeet.threedollar.api.controller.popup

import com.depromeet.threedollar.application.service.popup.dto.request.GetActivatedPopupsRequest
import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.application.mapper.popup.PopupPositionMapper
import com.depromeet.threedollar.application.mapper.popup.dto.response.PopupPositionResponse
import com.depromeet.threedollar.application.service.popup.PopupService
import com.depromeet.threedollar.application.service.popup.dto.response.PopupResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class PopupController(
    private val popupService: PopupService
) {

    @ApiOperation("팝업 위치별로 활성화중인 팝업 목록을 조회합니다")
    @GetMapping("/api/v1/popups")
    fun getActivatedPopups(
        @Valid request: GetActivatedPopupsRequest
    ): ApiResponse<List<PopupResponse>> {
        return ApiResponse.success(popupService.getActivatedPopups(request))
    }

    @ApiOperation("팝업 위치 목록을 조회합니다 (문서용)")
    @GetMapping("/api/v1/popup/positions")
    fun getPopupPositions(): ApiResponse<List<PopupPositionResponse>> {
        return ApiResponse.success(PopupPositionMapper.retrievePopupPositionTypes())
    }

}