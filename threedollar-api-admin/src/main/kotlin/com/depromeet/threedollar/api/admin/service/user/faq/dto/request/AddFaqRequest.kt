package com.depromeet.threedollar.api.admin.service.user.faq.dto.request

import com.depromeet.threedollar.domain.rds.user.domain.faq.Faq
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqCategory
import javax.validation.constraints.Size

data class AddFaqRequest(
    @field:Size(max = 100, message = "{faq.question.size}")
    val question: String,

    @field:Size(max = 200, message = "{faq.answer.size}")
    val answer: String,

    val category: FaqCategory
) {

    fun toEntity(): Faq {
        return Faq.newInstance(category, question, answer)
    }

}
