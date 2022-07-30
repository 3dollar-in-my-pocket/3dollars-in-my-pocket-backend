package com.depromeet.threedollar.api.adminservice.controller.bossservice.feedback

import com.depromeet.threedollar.api.adminservice.config.interceptor.Auth
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.feedback.dto.response.BossStoreFeedbackTypeResponse
import com.depromeet.threedollar.common.type.BossStoreFeedbackType
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BossStoreFeedbackController {

    @ApiOperation("사장님 가게 피드백의 타입 목록을 조회합니다")
    @Auth
    @GetMapping("/v1/boss/store/feedback/types")
    fun getBossStoreFeedbackTypes(): ApiResponse<List<BossStoreFeedbackTypeResponse>> {
        return ApiResponse.success(BossStoreFeedbackType.values().asSequence()
            .map { feedbackType -> BossStoreFeedbackTypeResponse.of(feedbackType) }
            .toList())
    }

}
