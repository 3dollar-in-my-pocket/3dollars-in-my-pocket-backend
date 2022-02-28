package com.depromeet.threedollar.boss.api.controller.feedback

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.boss.api.service.feedback.BossStoreFeedbackService
import com.depromeet.threedollar.boss.api.service.feedback.dto.request.GetBossStoreFeedbacksCountsBetweenDateRequest
import com.depromeet.threedollar.boss.api.service.feedback.dto.response.BossStoreFeedbackCountResponse
import com.depromeet.threedollar.boss.api.service.feedback.dto.response.BossStoreFeedbackGroupingDateResponse
import com.depromeet.threedollar.boss.api.service.feedback.dto.response.BossStoreFeedbackTypeResponse
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class BossStoreFeedbackController(
    private val bossStoreFeedbackService: BossStoreFeedbackService
) {

    @ApiOperation("전체 기간동안의 특정 사장님 가게의 피드백 갯수를 조회합니다.")
    @GetMapping("/v1/boss-store/{bossStoreId}/feedbacks/full")
    fun getBossStoreFeedbacksCounts(
        @PathVariable bossStoreId: String
    ): ApiResponse<List<BossStoreFeedbackCountResponse>> {
        return ApiResponse.success(bossStoreFeedbackService.getBossStoreFeedbacksCounts(bossStoreId))
    }

    @ApiOperation("특정 기간내의 특정 사장님 가게의 피드백 갯수를 조회합니다.")
    @GetMapping("/v1/boss-store/{bossStoreId}/feedbacks/specific")
    fun getBossStoreFeedbacksCountsBetweenDate(
        @PathVariable bossStoreId: String,
        @Valid request: GetBossStoreFeedbacksCountsBetweenDateRequest
    ): ApiResponse<List<BossStoreFeedbackGroupingDateResponse>> {
        request.validateRequestDateTimeInterval()
        return ApiResponse.success(bossStoreFeedbackService.getBossStoreFeedbacksCountsBetweenDate(bossStoreId, request))
    }

    @ApiOperation("사장님 가게 피드백의 타입 목록을 조회합니다")
    @GetMapping("/v1/boss-store/feedback/types")
    fun getBossStoreFeedbackTypes(): List<BossStoreFeedbackTypeResponse> {
        return BossStoreFeedbackType.values().asSequence()
            .map { BossStoreFeedbackTypeResponse.of(it) }
            .toList()
    }

}
