package com.depromeet.threedollar.application.service.faq

import com.depromeet.threedollar.application.config.cache.CacheConstants
import com.depromeet.threedollar.application.service.faq.dto.request.RetrieveFaqsRequest
import com.depromeet.threedollar.application.service.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.application.service.faq.dto.response.FaqResponse
import com.depromeet.threedollar.domain.domain.faq.FaqCategory
import com.depromeet.threedollar.domain.domain.faq.FaqRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FaqService(
    private val faqRepository: FaqRepository
) {

    @Cacheable(key = "#request.category?:'ALL'", value = [CacheConstants.CacheKeyName.FAQS])
    @Transactional(readOnly = true)
    fun retrieveAllFaqs(request: RetrieveFaqsRequest): List<FaqResponse> {
        return faqRepository.findAllByCategory(request.category).asSequence()
            .sortedBy { it.category.displayOrder }
            .map { FaqResponse.of(it) }
            .toList()
    }

    fun retrieveAllFaqCategories(): List<FaqCategoryResponse> {
        return FaqCategory.values().asSequence()
            .sortedBy { it.displayOrder }
            .map { FaqCategoryResponse.of(it) }
            .toList()
    }

}
