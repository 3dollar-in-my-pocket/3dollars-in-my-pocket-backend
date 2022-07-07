package com.depromeet.threedollar.api.core.service.commonservice.faq

import com.depromeet.threedollar.api.core.service.commonservice.faq.dto.request.RetrieveFaqsRequest
import com.depromeet.threedollar.api.core.service.commonservice.faq.dto.response.FaqResponse
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.type.CacheType.CacheKey.FAQS
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FaqService(
    private val faqRepository: FaqRepository,
) {

    @Cacheable(cacheNames = [FAQS], key = "{#applicationType, #request.category?:'ALL'}")
    @Transactional(readOnly = true)
    fun retrieveFaqsByCategory(applicationType: ApplicationType, request: RetrieveFaqsRequest): List<FaqResponse> {
        validateSupportCategory(applicationType, request.category)
        return faqRepository.findAllByApplicationTypeAndCategory(applicationType, request.category).asSequence()
            .sortedBy { faq -> faq.category.displayOrder }
            .map { faq -> FaqResponse.of(faq) }
            .toList()
    }

    private fun validateSupportCategory(applicationType: ApplicationType, category: FaqCategory?) {
        if (category == null) {
            return
        }
        if (!category.isSupported(applicationType)) {
            throw ForbiddenException("해당 서비스(${applicationType})에서 지원하지 않는 FAQ 카테고리(${category}) 입니다", ErrorCode.FORBIDDEN_NOT_SUPPORTED_FAQ_CATEGORY)
        }
    }

}
