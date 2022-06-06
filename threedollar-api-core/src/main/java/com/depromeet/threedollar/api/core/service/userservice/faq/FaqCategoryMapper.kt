package com.depromeet.threedollar.api.core.service.userservice.faq

import com.depromeet.threedollar.api.core.service.userservice.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory

object FaqCategoryMapper {

    fun retrieveFaqCategories(): List<FaqCategoryResponse> {
        return FaqCategory.values().asSequence()
            .sortedBy { faqCategory -> faqCategory.displayOrder }
            .map { faqCategory -> FaqCategoryResponse.of(faqCategory) }
            .toList()
    }

}
