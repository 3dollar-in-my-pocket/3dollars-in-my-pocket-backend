package com.depromeet.threedollar.application.mapper.faq

import com.depromeet.threedollar.application.mapper.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.domain.domain.faq.FaqCategory

object FaqCategoryMapper {

    fun retrieveAllFaqCategories(): List<FaqCategoryResponse> {
        return FaqCategory.values().asSequence()
            .sortedBy { it.displayOrder }
            .map { FaqCategoryResponse.of(it) }
            .toList()
    }

}
