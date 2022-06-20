package com.depromeet.threedollar.api.adminservice.controller.commonservice.faq

import javax.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.depromeet.threedollar.api.adminservice.config.interceptor.Auth
import com.depromeet.threedollar.api.adminservice.service.commonservice.faq.FaqAdminService
import com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.response.FaqAdminResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.mapper.commonservice.faq.FaqCategoryMapper
import com.depromeet.threedollar.api.core.mapper.commonservice.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.api.core.service.commonservice.faq.dto.response.FaqResponse
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory
import io.swagger.annotations.ApiOperation

@RestController
class AdminFaqController(
    private val faqAdminService: FaqAdminService,
) {

    @ApiOperation("새로운 FAQ를 등록합니다")
    @Auth
    @PostMapping("/v1/faq")
    fun addFaq(
        @Valid @RequestBody request: AddFaqRequest,
    ): ApiResponse<FaqResponse> {
        return ApiResponse.success(faqAdminService.addFaq(request))
    }

    @ApiOperation("특정 FAQ를 수정합니다")
    @Auth
    @PutMapping("/v1/faq/{faqId}")
    fun updateFaq(
        @PathVariable faqId: Long,
        @Valid @RequestBody request: UpdateFaqRequest,
    ): ApiResponse<FaqResponse> {
        return ApiResponse.success(faqAdminService.updateFaq(faqId, request))
    }

    @ApiOperation("특정 FAQ를 삭제합니다")
    @Auth
    @DeleteMapping("/v1/faq/{faqId}")
    fun deleteFaq(
        @PathVariable faqId: Long,
    ): ApiResponse<String> {
        faqAdminService.deleteFaq(faqId)
        return ApiResponse.OK
    }

    @ApiOperation("FAQ 목록을 조회합니다")
    @GetMapping("/v1/faqs")
    fun retrieveFaqs(
        @RequestParam(required = false) applicationType: ApplicationType?,
        @RequestParam(required = false) category: FaqCategory?,
    ): ApiResponse<List<FaqAdminResponse>> {
        val response = faqAdminService.retrieveAllFaqs(applicationType, category)
        return ApiResponse.success(response)
    }

    @ApiOperation("FAQ 카테고리 목록을 조회합니다")
    @GetMapping("/v1/faq/categories")
    fun retrieveFaqCategories(
        @RequestParam applicationType: ApplicationType,
    ): ApiResponse<List<FaqCategoryResponse>> {
        return ApiResponse.success(FaqCategoryMapper.retrieveFaqCategories(applicationType))
    }

}
