package com.depromeet.threedollar.api.controller.menu

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.application.mapper.MenuCategoryMapper
import com.depromeet.threedollar.application.mapper.dto.MenuCategoryResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MenuCategoryController {

    @GetMapping("/api/v2/store/menu/categories")
    fun retrieveStoreMenuCategories(): ApiResponse<List<MenuCategoryResponse>> {
        return ApiResponse.success(MenuCategoryMapper.retrieveMenuCategories())
    }

}
