package com.depromeet.threedollar.admin.service.faq.dto.request

import com.depromeet.threedollar.domain.domain.faq.Faq
import com.depromeet.threedollar.domain.domain.faq.FaqCategory
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class AddFaqRequest(
    @get:NotNull
    val category: FaqCategory? = null,

    @get:NotBlank
    val question: String = " ",

    @get:NotBlank
    val answer: String = ""
) {

    fun toEntity(): Faq {
        return Faq.newInstance(category, question, answer)
    }

}
