package com.depromeet.threedollar.api.admin.service.user.faq.dto.request

import com.depromeet.threedollar.domain.rds.user.domain.faq.Faq
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqCategory
import org.hibernate.validator.constraints.Length

data class AddFaqRequest(
    @field:Length(max = 100, message = "{faq.question.length}")
    val question: String,

    @field:Length(max = 200, message = "{faq.answer.length}")
    val answer: String,

    val category: FaqCategory
) {

    fun toEntity(): Faq {
        return Faq.newInstance(category, question, answer)
    }

}