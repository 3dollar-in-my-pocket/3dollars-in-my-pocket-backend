package com.depromeet.threedollar.admin.service.faq.dto.request

import com.depromeet.threedollar.domain.domain.faq.FaqCategory
import javax.validation.constraints.NotBlank

data class UpdateFaqRequest(
    @get:NotBlank
    val question: String = "",

    @get:NotBlank
    val answer: String = "",

    val category: FaqCategory
)
