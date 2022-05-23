package com.depromeet.threedollar.api.vendor.controller.store

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.mapper.user.menu.MenuCategoryMapper
import com.depromeet.threedollar.api.core.mapper.user.menu.dto.response.MenuCategoryResponse
import io.swagger.annotations.ApiOperation

@RestController
class StoreMenuCategoryController {

    @ApiOperation("현재 활성화중인 메뉴 카테고리 목록들을 조회합니다.")
    @GetMapping("/v2/store/menu/categories")
    fun retrieveActiveMenuCategories(): ApiResponse<List<MenuCategoryResponse>> {
        return ApiResponse.success(MenuCategoryMapper.retrieveActiveMenuCategories())
    }

}
