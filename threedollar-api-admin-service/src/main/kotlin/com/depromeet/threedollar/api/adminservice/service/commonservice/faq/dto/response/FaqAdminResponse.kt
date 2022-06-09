package com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.response

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.Faq
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory

data class FaqAdminResponse(
    val faqId: Long,
    val applicationType: ApplicationType,
    val question: String,
    val answer: String,
    val category: FaqCategory,
) : AuditingTimeResponse() {

    companion object {
        fun of(faq: Faq): FaqAdminResponse {
            val response = FaqAdminResponse(
                faqId = faq.id,
                applicationType = faq.applicationType,
                question = faq.question,
                answer = faq.answer,
                category = faq.category
            )
            response.setAuditingTimeByEntity(faq)
            return response
        }
    }

}
