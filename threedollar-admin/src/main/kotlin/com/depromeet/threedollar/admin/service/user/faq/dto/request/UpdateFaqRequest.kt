package com.depromeet.threedollar.admin.service.user.faq.dto.request

import com.depromeet.threedollar.domain.user.domain.faq.FaqCategory
import org.hibernate.validator.constraints.Length

data class UpdateFaqRequest(
    @field:Length(max = 100, message = "{faq.question.length}")
    val question: String,

    @field:Length(max = 200, message = "{faq.answer.length}")
    val answer: String,

    val category: FaqCategory
)
