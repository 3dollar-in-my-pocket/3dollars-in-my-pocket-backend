package com.depromeet.threedollar.api.boss.controller.category

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.boss.category.BossStoreCategoryService
import com.depromeet.threedollar.api.core.service.boss.category.dto.response.BossStoreCategoryResponse
import io.swagger.annotations.ApiOperation

@RestController
class BossStoreCategoryController(
    private val bossStoreCategoryService: BossStoreCategoryService,
) {

    @ApiOperation("사장님 가게의 카테고리 목록을 조회합니다")
    @GetMapping("/v1/boss/store/categories")
    fun retrieveBossCategories(): ApiResponse<List<BossStoreCategoryResponse>> {
        return ApiResponse.success(bossStoreCategoryService.retrieveBossStoreCategories())
    }

}
