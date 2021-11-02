package com.depromeet.threedollar.admin.controller.faq

import com.depromeet.threedollar.admin.service.faq.FaqAdminService
import com.depromeet.threedollar.admin.service.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.admin.service.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.application.mapper.faq.FaqCategoryMapper
import com.depromeet.threedollar.application.mapper.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.application.service.faq.FaqService
import com.depromeet.threedollar.application.service.faq.dto.request.RetrieveFaqsRequest
import com.depromeet.threedollar.application.service.faq.dto.response.FaqResponse
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class FaqAdminController(
    private val faqAdminService: FaqAdminService,
    private val faqService: FaqService
) {

    @PostMapping("/admin/v1/faq")
    fun addFaq(
        @Valid @RequestBody request: AddFaqRequest
    ): ApiResponse<FaqResponse> {
        return ApiResponse.success(faqAdminService.addFaq(request))
    }

    @PutMapping("/admin/v1/faq/{faqId}")
    fun updateFaq(
        @PathVariable faqId: Long,
        @Valid @RequestBody request: UpdateFaqRequest
    ): ApiResponse<FaqResponse> {
        return ApiResponse.success(faqAdminService.updateFaq(faqId, request))
    }

    @DeleteMapping("/admin/v1/faq/{faqId}")
    fun deleteFaq(
        @PathVariable faqId: Long
    ): ApiResponse<String> {
        faqAdminService.deleteFaq(faqId)
        return ApiResponse.SUCCESS
    }

    @GetMapping("/admin/v1/faqs")
    fun retrieveFaqs(
        @Valid request: RetrieveFaqsRequest
    ): ApiResponse<List<FaqResponse>> {
        return ApiResponse.success(faqService.retrieveAllFaqs(request))
    }

    @GetMapping("/admin/v1/faq/categories")
    fun retrieveFaqCategories(): ApiResponse<List<FaqCategoryResponse>> {
        return ApiResponse.success(FaqCategoryMapper.retrieveAllFaqCategories())
    }

}
