package com.depromeet.threedollar.admin.service.faq.dto.request

import com.depromeet.threedollar.domain.user.domain.faq.Faq
import com.depromeet.threedollar.domain.user.domain.faq.FaqCategory

data class AddFaqRequest(
    val question: String,
    val answer: String,
    val category: FaqCategory
) {

    fun toEntity(): Faq {
        return Faq.newInstance(category, question, answer)
    }

}
