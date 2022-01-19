package com.depromeet.threedollar.admin.controller.popup

import com.depromeet.threedollar.admin.service.popup.PopupAdminService
import com.depromeet.threedollar.admin.service.popup.dto.request.AddPopupRequest
import com.depromeet.threedollar.admin.service.popup.dto.request.UpdatePopupRequest
import com.depromeet.threedollar.application.common.dto.ApiResponse
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class PopupController(
    private val popupAdminService: PopupAdminService
) {

    @PostMapping("/admin/v1/popup")
    fun addPopup(
        @Valid @RequestBody request: AddPopupRequest
    ): ApiResponse<String> {
        popupAdminService.addPopup(request)
        return ApiResponse.SUCCESS
    }

    @PutMapping("/admin/v1/popup/{popupId}")
    fun updatePopup(
        @PathVariable popupId: Long,
        @Valid @RequestBody request: UpdatePopupRequest
    ): ApiResponse<String> {
        popupAdminService.updatePopup(popupId, request)
        return ApiResponse.SUCCESS
    }

    @DeleteMapping("/admin/v1/popup/{popupId}")
    fun deletePopup(
        @PathVariable popupId: Long
    ): ApiResponse<String> {
        popupAdminService.deletePopup(popupId)
        return ApiResponse.SUCCESS
    }

    // TODO: 페이지네이션으로 팝업 목록을 조회하는 API 추가

}
