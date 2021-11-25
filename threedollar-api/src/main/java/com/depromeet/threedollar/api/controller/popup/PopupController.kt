package com.depromeet.threedollar.api.controller.popup

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.application.service.popup.PopupService
import com.depromeet.threedollar.application.service.popup.dto.response.PopupResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PopupController(
    private val popupService: PopupService
) {

    @ApiOperation("활성화중인 팝업 목록을 보여줍니다.")
    @GetMapping("/api/v1/popups")
    fun getActivatedPopups(): ApiResponse<List<PopupResponse>> {
        return ApiResponse.success(popupService.getActivatedPopups())
    }

}
