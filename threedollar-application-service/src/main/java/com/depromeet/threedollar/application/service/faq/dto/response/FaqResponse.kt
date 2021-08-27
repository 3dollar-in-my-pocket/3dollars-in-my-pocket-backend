package com.depromeet.threedollar.application.service.faq.dto.response

import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.domain.domain.faq.Faq
import com.depromeet.threedollar.domain.domain.faq.FaqCategory
import java.io.Serializable

data class FaqResponse(
    val faqId: Long,
    val question: String,
    val answer: String,
    val category: FaqCategory
) : AuditingTimeResponse(), Serializable {

    companion object {
        fun of(faq: Faq): FaqResponse {
            val response = FaqResponse(faq.id, faq.question, faq.answer, faq.category)
            response.setBaseTime(faq)
            return response
        }
    }

}
