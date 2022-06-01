package com.depromeet.threedollar.api.core.mapper.user.faq

import com.depromeet.threedollar.api.core.mapper.user.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqCategory

object FaqCategoryMapper {

    fun retrieveFaqCategories(): List<FaqCategoryResponse> {
        return FaqCategory.values().asSequence()
            .sortedBy { faqCategory -> faqCategory.displayOrder }
            .map { faqCategory -> FaqCategoryResponse.of(faqCategory) }
            .toList()
    }

}
