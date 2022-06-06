package com.depromeet.threedollar.api.core.service.userservice.faq

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.core.service.userservice.faq.dto.request.RetrieveFaqsRequest
import com.depromeet.threedollar.api.core.service.userservice.faq.dto.response.FaqResponse
import com.depromeet.threedollar.common.type.CacheType.CacheKey.FAQS
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqRepository

@Service
class FaqService(
    private val faqRepository: FaqRepository,
) {

    @Cacheable(cacheNames = [FAQS], key = "#request.category?:'ALL'")
    @Transactional(readOnly = true)
    fun retrieveFaqsByCategory(request: RetrieveFaqsRequest): List<FaqResponse> {
        return faqRepository.findAllByCategory(request.category).asSequence()
            .sortedBy { faq -> faq.category.displayOrder }
            .map { faq -> FaqResponse.of(faq) }
            .toList()
    }

}
