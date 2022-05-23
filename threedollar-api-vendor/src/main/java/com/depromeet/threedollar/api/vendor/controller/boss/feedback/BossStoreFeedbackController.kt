package com.depromeet.threedollar.api.vendor.controller.boss.feedback

import java.time.LocalDate
import javax.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.boss.feedback.BossStoreFeedbackService
import com.depromeet.threedollar.api.core.service.boss.feedback.dto.request.AddBossStoreFeedbackRequest
import com.depromeet.threedollar.api.core.service.boss.feedback.dto.response.BossStoreFeedbackCountWithRatioResponse
import com.depromeet.threedollar.api.core.service.boss.feedback.dto.response.BossStoreFeedbackTypeResponse
import com.depromeet.threedollar.api.vendor.config.interceptor.Auth
import com.depromeet.threedollar.api.vendor.config.resolver.UserId
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import io.swagger.annotations.ApiOperation

@RestController
class BossStoreFeedbackController(
    private val bossStoreFeedbackService: BossStoreFeedbackService,
) {

    @ApiOperation("전체 기간동안의 특정 사장님 가게의 피드백 갯수를 조회합니다.")
    @GetMapping("/v1/boss/store/{bossStoreId}/feedbacks/full")
    fun getBossStoreFeedbacksCounts(
        @PathVariable bossStoreId: String,
    ): ApiResponse<List<BossStoreFeedbackCountWithRatioResponse>> {
        return ApiResponse.success(bossStoreFeedbackService.getBossStoreFeedbacksCounts(bossStoreId))
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
        @UserId userId: Long?,
    ): ApiResponse<String> {
        bossStoreFeedbackService.addFeedback(
            bossStoreId = bossStoreId,
            userId = userId!!,
            request = request,
            date = LocalDate.now())
        return ApiResponse.OK
    }

}
