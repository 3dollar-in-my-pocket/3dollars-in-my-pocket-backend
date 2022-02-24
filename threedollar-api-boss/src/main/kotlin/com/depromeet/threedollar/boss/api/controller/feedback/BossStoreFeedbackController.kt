package com.depromeet.threedollar.boss.api.controller.feedback

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.service.feedback.BossStoreFeedbackService
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class BossStoreFeedbackController(
    private val bossStoreFeedbackService: BossStoreFeedbackService
) {

    @ApiOperation("특정 사장님 가게의 전체 피드백 갯수를 조회합니다.")
    @GetMapping("/v1/boss-store/{bossStoreId}/feedback")
    fun getBossStoreFeedbacksCounts(
        @PathVariable bossStoreId: String
    ): ApiResponse<Map<BossStoreFeedbackType, Long>> {
        return ApiResponse.success(bossStoreFeedbackService.getBossStoreFeedbacksCounts(bossStoreId))
    }

}
