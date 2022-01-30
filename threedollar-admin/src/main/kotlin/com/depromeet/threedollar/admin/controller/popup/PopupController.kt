package com.depromeet.threedollar.admin.controller.popup

import com.depromeet.threedollar.admin.service.popup.PopupAdminService
import com.depromeet.threedollar.admin.service.popup.dto.request.AddPopupRequest
import com.depromeet.threedollar.admin.service.popup.dto.request.UpdatePopupRequest
import com.depromeet.threedollar.admin.service.popup.dto.response.PopupsWithPagingResponse
import com.depromeet.threedollar.application.common.dto.ApiResponse
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class PopupController(
    private val popupAdminService: PopupAdminService
) {

    @PostMapping("/v1/popup")
    fun addPopup(
        @Valid @RequestBody request: AddPopupRequest
    ): ApiResponse<String> {
        popupAdminService.addPopup(request)
        return ApiResponse.SUCCESS
    }

    @PutMapping("/v1/popup/{popupId}")
    fun updatePopup(
        @PathVariable popupId: Long,
        @Valid @RequestBody request: UpdatePopupRequest
    ): ApiResponse<String> {
        popupAdminService.updatePopup(popupId, request)
        return ApiResponse.SUCCESS
    }

    @DeleteMapping("/v1/popup/{popupId}")
    fun deletePopup(
        @PathVariable popupId: Long
    ): ApiResponse<String> {
        popupAdminService.deletePopup(popupId)
        return ApiResponse.SUCCESS
    }

    @GetMapping("/v1/popups")
    fun getPopups(
        @RequestParam size: Long,
        @RequestParam page: Int
    ): ApiResponse<PopupsWithPagingResponse> {
        return ApiResponse.success(popupAdminService.getPopups(
            size = size,
            page = page
        ))
    }

}
