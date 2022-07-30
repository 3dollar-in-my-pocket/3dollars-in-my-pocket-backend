package com.depromeet.threedollar.api.core.service.mapper.commonservice.faq

import com.depromeet.threedollar.api.core.service.mapper.commonservice.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory

object FaqCategoryMapper {

    fun retrieveFaqCategories(applicationType: ApplicationType): List<FaqCategoryResponse> {
        return FaqCategory.values().asSequence()
            .filter { faqCategory -> faqCategory.isSupported(applicationType) }
            .sortedBy { faqCategory -> faqCategory.displayOrder }
            .map { faqCategory -> FaqCategoryResponse.of(faqCategory) }
            .toList()
    }

}
