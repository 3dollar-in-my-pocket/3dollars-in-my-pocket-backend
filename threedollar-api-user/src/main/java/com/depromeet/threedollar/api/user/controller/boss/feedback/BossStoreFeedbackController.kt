package com.depromeet.threedollar.api.user.controller.boss.feedback

import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.feedback.BossStoreFeedbackService
import com.depromeet.threedollar.api.core.service.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.api.core.service.feedback.dto.request.GetBossStoreFeedbacksCountsBetweenDateRequest
import com.depromeet.threedollar.api.core.service.feedback.dto.response.BossStoreFeedbackCountResponse
import com.depromeet.threedollar.api.core.service.feedback.dto.response.BossStoreFeedbackCursorResponse
import com.depromeet.threedollar.api.core.service.feedback.dto.response.BossStoreFeedbackTypeResponse
import com.depromeet.threedollar.api.user.config.interceptor.Auth
import com.depromeet.threedollar.api.user.config.resolver.UserId
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.Valid

@RestController
class BossStoreFeedbackController(
    private val bossStoreFeedbackService: BossStoreFeedbackService
) {

    @ApiOperation("전체 기간동안의 특정 사장님 가게의 피드백 갯수를 조회합니다.")
    @GetMapping("/v1/boss/store/{bossStoreId}/feedbacks/full")
    fun getBossStoreFeedbacksCounts(
        @PathVariable bossStoreId: String
    ): ApiResponse<List<BossStoreFeedbackCountResponse>> {
        return ApiResponse.success(bossStoreFeedbackService.getBossStoreFeedbacksCounts(bossStoreId))
    }

    @ApiOperation("특정 기간내의 특정 사장님 가게의 피드백 갯수를 조회합니다. (스크롤 페이지네이션)", notes = "https://github.com/3dollar-in-my-pocket/3dollars-in-my-pocket-backend/issues/122")
    @GetMapping("/v1/boss/store/{bossStoreId}/feedbacks/specific")
    fun getBossStoreFeedbacksCountsBetweenDate(
        @PathVariable bossStoreId: String,
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate
    ): ApiResponse<BossStoreFeedbackCursorResponse> {
        val request = GetBossStoreFeedbacksCountsBetweenDateRequest(startDate = startDate, endDate = endDate)
        return ApiResponse.success(bossStoreFeedbackService.getBossStoreFeedbacksCountsBetweenDate(bossStoreId, request))
    }

    @ApiOperation("사장님 가게 피드백의 타입 목록을 조회합니다")
    @GetMapping("/v1/boss/store/feedback/types")
    fun getBossStoreFeedbackTypes(): ApiResponse<List<BossStoreFeedbackTypeResponse>> {
        return ApiResponse.success(BossStoreFeedbackType.values().asSequence()
            .map { BossStoreFeedbackTypeResponse.of(it) }
            .toList())
    }

    @ApiOperation("특정 사장님 가게에 피드백을 추가합니다")
    @Auth
    @PostMapping("/v1/boss/store/{bossStoreId}/feedback")
    fun addBossStoreFeedback(
        @PathVariable bossStoreId: String,
        @Valid @RequestBody request: AddBossStoreFeedbackRequest,
        @UserId userId: Long?
    ): ApiResponse<String> {
        bossStoreFeedbackService.addFeedback(
            bossStoreId = bossStoreId,
            userId = userId ?: 0L,
            request = request,
            date = LocalDate.now())
        return ApiResponse.SUCCESS
    }

}
