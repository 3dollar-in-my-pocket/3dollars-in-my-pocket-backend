package com.depromeet.threedollar.boss.api.controller.feedback

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.service.feedback.BossStoreFeedbackService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class BossStoreFeedbackController(
    private val bossStoreFeedbackService: BossStoreFeedbackService
) {

    @ApiOperation("특정 사장님의 가게의 피드백을 조회합니다.")
    @GetMapping("/v1/boss-store/feedback/{bossStoreId}")
    fun getBossStoreFeedbacks(
        @PathVariable bossStoreId: String
    ): ApiResponse<Map<String, Long>> {
        return ApiResponse.success(bossStoreFeedbackService.retrieveBossStoreFeedbacks(bossStoreId))
    }

}
