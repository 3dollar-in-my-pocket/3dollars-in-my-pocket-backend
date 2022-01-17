package com.depromeet.threedollar.api.controller.displaymenu

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.application.service.displaymenu.DisplayMenuCategoryService
import com.depromeet.threedollar.application.service.displaymenu.dto.response.DisplayMenuCategoryResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DisplayMenuCategoryController(
    private val displayMenuCategoryService: DisplayMenuCategoryService
) {

    @ApiOperation("현재 활성화중인 메뉴 카테고리 목록들을 조회합니다.")
    @GetMapping("/api/v2/store/menu/categories")
    fun getDisplayMenuCategories(): ApiResponse<List<DisplayMenuCategoryResponse>> {
        return ApiResponse.success(displayMenuCategoryService.getDisplayMenuCategories())
    }

}
