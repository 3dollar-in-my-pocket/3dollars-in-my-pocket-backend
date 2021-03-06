package com.depromeet.threedollar.api.bossservice.controller.feedback

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.BossStoreFeedbackRetrieveService
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.request.GetBossStoreFeedbacksCountsBetweenDateRequest
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.response.BossStoreFeedbackCountWithRatioResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.response.BossStoreFeedbackCursorResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.response.BossStoreFeedbackTypeResponse
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class BossStoreFeedbackController(
    private val bossStoreFeedbackRetrieveService: BossStoreFeedbackRetrieveService,
) {

    @ApiOperation("전체 기간동안의 특정 사장님 가게의 피드백 갯수를 조회합니다.")
    @GetMapping("/v1/boss/store/{bossStoreId}/feedbacks/full")
    fun getBossStoreFeedbacksCounts(
        @PathVariable bossStoreId: String,
    ): ApiResponse<List<BossStoreFeedbackCountWithRatioResponse>> {
        return ApiResponse.success(bossStoreFeedbackRetrieveService.getBossStoreFeedbacksCounts(bossStoreId))
    }

    @ApiOperation("특정 기간내의 특정 사장님 가게의 피드백 갯수를 조회합니다. (스크롤 페이지네이션)", notes = "https://github.com/3dollar-in-my-pocket/3dollars-in-my-pocket-backend/issues/122")
    @GetMapping("/v1/boss/store/{bossStoreId}/feedbacks/specific")
    fun getBossStoreFeedbacksCountsBetweenDate(
        @PathVariable bossStoreId: String,
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate,
    ): ApiResponse<BossStoreFeedbackCursorResponse> {
        val request = GetBossStoreFeedbacksCountsBetweenDateRequest(startDate = startDate, endDate = endDate)
        return ApiResponse.success(bossStoreFeedbackRetrieveService.getBossStoreFeedbacksCountsBetweenDate(bossStoreId, request))
    }

    @ApiOperation("사장님 가게 피드백의 타입 목록을 조회합니다")
    @GetMapping("/v1/boss/store/feedback/types")
    fun getBossStoreFeedbackTypes(): ApiResponse<List<BossStoreFeedbackTypeResponse>> {
        return ApiResponse.success(BossStoreFeedbackType.values().asSequence()
            .map { feedbackType -> BossStoreFeedbackTypeResponse.of(feedbackType) }
            .toList())
    }

}
