package com.depromeet.threedollar.api.bossservice.controller.category

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.category.BossStoreCategoryRetrieveService
import com.depromeet.threedollar.api.core.service.service.bossservice.category.dto.response.BossStoreCategoryResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BossStoreCategoryController(
    private val bossStoreCategoryRetrieveService: BossStoreCategoryRetrieveService,
) {

    @ApiOperation("사장님 가게의 카테고리 목록을 조회합니다")
    @GetMapping("/v1/boss/store/categories")
    fun retrieveBossCategories(): ApiResponse<List<BossStoreCategoryResponse>> {
        return ApiResponse.success(bossStoreCategoryRetrieveService.retrieveBossStoreCategories())
    }

}
