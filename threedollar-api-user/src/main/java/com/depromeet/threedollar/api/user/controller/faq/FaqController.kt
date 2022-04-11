package com.depromeet.threedollar.api.user.controller.faq

import javax.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.mapper.user.faq.FaqCategoryMapper
import com.depromeet.threedollar.api.core.mapper.user.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.api.core.service.user.faq.FaqService
import com.depromeet.threedollar.api.core.service.user.faq.dto.request.RetrieveFaqsRequest
import com.depromeet.threedollar.api.core.service.user.faq.dto.response.FaqResponse
import io.swagger.annotations.ApiOperation

@RestController
class FaqController(
    private val faqService: FaqService
) {

    @ApiOperation("특정 카테고리의 FAQ 목록을 조회합니다")
    @GetMapping("/v2/faqs")
    fun retrieveFaqsByCategory(
        @Valid request: RetrieveFaqsRequest
    ): ApiResponse<List<FaqResponse>> {
        return ApiResponse.success(faqService.retrieveFaqsByCategory(request))
    }

    @ApiOperation("모든 FAQ 카테고리 목록을 조회합니다")
    @GetMapping("/v2/faq/categories")
    fun retrieveFaqCategories(): ApiResponse<List<FaqCategoryResponse>> {
        return ApiResponse.success(FaqCategoryMapper.retrieveFaqCategories())
    }

}
