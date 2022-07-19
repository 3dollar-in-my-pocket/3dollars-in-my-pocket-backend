package com.depromeet.threedollar.api.core.service.commonservice.faq.dto.response

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.api.core.mapper.commonservice.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.Faq
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory

data class FaqResponse(
    val faqId: Long,
    val question: String,
    val answer: String,
    @Deprecated(message = "v3.0.6부터 categoryInfo로 대체")
    val category: FaqCategory,
    val categoryInfo: FaqCategoryResponse,
) : AuditingTimeResponse() {

    companion object {
        fun of(faq: Faq): FaqResponse {
            val response = FaqResponse(
                faqId = faq.id,
                question = faq.question,
                answer = faq.answer,
                category = faq.category,
                categoryInfo = FaqCategoryResponse.of(faq.category)
            )
            response.setAuditingTimeByEntity(faq)
            return response
        }
    }

}
