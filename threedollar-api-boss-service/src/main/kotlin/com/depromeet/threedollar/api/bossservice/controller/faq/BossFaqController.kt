package com.depromeet.threedollar.api.bossservice.controller.faq

import javax.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.commonservice.faq.FaqCategoryMapper
import com.depromeet.threedollar.api.core.service.commonservice.faq.FaqService
import com.depromeet.threedollar.api.core.service.commonservice.faq.dto.request.RetrieveFaqsRequest
import com.depromeet.threedollar.api.core.service.commonservice.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.api.core.service.commonservice.faq.dto.response.FaqResponse
import com.depromeet.threedollar.common.type.ApplicationType
import io.swagger.annotations.ApiOperation

@RestController
class BossFaqController(
    private val faqService: FaqService,
) {

    @ApiOperation("FAQ 목록을 조회합니다")
    @GetMapping("/v1/faqs")
    fun retrieveFaqsByCategory(
        @Valid request: RetrieveFaqsRequest,
    ): ApiResponse<List<FaqResponse>> {
        val faqs = faqService.retrieveFaqsByCategory(applicationType = ApplicationType.BOSS_API, request = request)
        return ApiResponse.success(faqs)
    }

    @ApiOperation("FAQ 카테고리 목록을 조회합니다")
    @GetMapping("/v1/faq/categories")
    fun retrieveFaqCategories(): ApiResponse<List<FaqCategoryResponse>> {
        val faqCategories = FaqCategoryMapper.retrieveFaqCategories(applicationType = ApplicationType.BOSS_API)
        return ApiResponse.success(faqCategories)
    }

}
