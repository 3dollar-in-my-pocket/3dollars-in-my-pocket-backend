package com.depromeet.threedollar.api.core.service.faq.dto.response

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.rds.user.domain.faq.Faq
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqCategory

data class FaqResponse(
    val faqId: Long,
    val question: String,
    val answer: String,
    val category: FaqCategory
) : AuditingTimeResponse() {

    companion object {
        fun of(faq: Faq): FaqResponse {
            val response = FaqResponse(
                faqId = faq.id,
                question = faq.question,
                answer = faq.answer,
                category = faq.category
            )
            response.setBaseTime(faq)
            return response
        }
    }

}
