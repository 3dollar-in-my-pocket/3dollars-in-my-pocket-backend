package com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.response

import com.depromeet.threedollar.api.core.service.common.dto.response.AuditingTimeResponse
import com.depromeet.threedollar.api.core.service.mapper.commonservice.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.Faq

data class FaqAdminResponse(
    val faqId: Long,
    val applicationType: ApplicationType,
    val question: String,
    val answer: String,
    val category: FaqCategoryResponse,
) : AuditingTimeResponse() {

    companion object {
        fun of(faq: Faq): FaqAdminResponse {
            val response = FaqAdminResponse(
                faqId = faq.id,
                applicationType = faq.applicationType,
                question = faq.question,
                answer = faq.answer,
                category = FaqCategoryResponse.of(faq.category),
            )
            response.setAuditingTimeByEntity(faq)
            return response
        }
    }

}
