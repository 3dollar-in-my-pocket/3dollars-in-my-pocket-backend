package com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.request

import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory
import javax.validation.constraints.Size

data class UpdateFaqRequest(
    @field:Size(max = 100, message = "{faq.question.size}")
    val question: String,

    @field:Size(max = 200, message = "{faq.answer.size}")
    val answer: String,

    val category: FaqCategory,
)
