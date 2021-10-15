package com.depromeet.threedollar.api.controller.faq

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.application.mapper.FaqCategoryMapper
import com.depromeet.threedollar.application.mapper.dto.FaqCategoryResponse
import com.depromeet.threedollar.application.service.faq.FaqService
import com.depromeet.threedollar.application.service.faq.dto.request.RetrieveFaqsRequest
import com.depromeet.threedollar.application.service.faq.dto.response.FaqResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FaqController(
    private val faqService: FaqService
) {

    @ApiOperation("모든 특정 카테고리의 FAQ를 조회합니다.")
    @GetMapping("/api/v2/faqs")
    fun retrieveAllFaqs(
        request: RetrieveFaqsRequest
    ): ApiResponse<List<FaqResponse>> {
        return ApiResponse.success(faqService.retrieveAllFaqs(request))
    }

    @ApiOperation("모든 FAQ 카테고리를 조회합니다.")
    @GetMapping("/api/v2/faq/categories")
    fun retrieveAllFaqCategories(): ApiResponse<List<FaqCategoryResponse>> {
        return ApiResponse.success(FaqCategoryMapper.retrieveAllFaqCategories())
    }

}
