package com.depromeet.threedollar.api.adminservice.controller.userservice.faq

import javax.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.adminservice.config.interceptor.Auth
import com.depromeet.threedollar.api.adminservice.service.userservice.faq.FaqAdminService
import com.depromeet.threedollar.api.adminservice.service.userservice.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.api.adminservice.service.userservice.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.userservice.faq.FaqCategoryMapper
import com.depromeet.threedollar.api.core.service.userservice.faq.FaqService
import com.depromeet.threedollar.api.core.service.userservice.faq.dto.request.RetrieveFaqsRequest
import com.depromeet.threedollar.api.core.service.userservice.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.api.core.service.userservice.faq.dto.response.FaqResponse
import io.swagger.annotations.ApiOperation

@RestController
class AdminFaqController(
    private val faqAdminService: FaqAdminService,
    private val faqService: FaqService,
) {

    @ApiOperation("새로운 FAQ를 등록합니다")
    @Auth
    @PostMapping("/v1/user/faq")
    fun addFaq(
        @Valid @RequestBody request: AddFaqRequest,
    ): ApiResponse<FaqResponse> {
        return ApiResponse.success(faqAdminService.addFaq(request))
    }

    @ApiOperation("특정 FAQ를 수정합니다")
    @Auth
    @PutMapping("/v1/user/faq/{faqId}")
    fun updateFaq(
        @PathVariable faqId: Long,
        @Valid @RequestBody request: UpdateFaqRequest,
    ): ApiResponse<FaqResponse> {
        return ApiResponse.success(faqAdminService.updateFaq(faqId, request))
    }

    @ApiOperation("특정 FAQ를 삭제합니다")
    @Auth
    @DeleteMapping("/v1/user/faq/{faqId}")
    fun deleteFaq(
        @PathVariable faqId: Long,
    ): ApiResponse<String> {
        faqAdminService.deleteFaq(faqId)
        return ApiResponse.OK
    }

    @ApiOperation("FAQ 목록을 조회합니다")
    @GetMapping("/v1/user/faqs")
    fun retrieveFaqs(
        @Valid request: RetrieveFaqsRequest,
    ): ApiResponse<List<FaqResponse>> {
        return ApiResponse.success(faqService.retrieveFaqsByCategory(request))
    }

    @ApiOperation("FAQ 카테고리 목록을 조회합니다")
    @GetMapping("/v1/user/faq/categories")
    fun retrieveFaqCategories(): ApiResponse<List<FaqCategoryResponse>> {
        return ApiResponse.success(FaqCategoryMapper.retrieveFaqCategories())
    }

}