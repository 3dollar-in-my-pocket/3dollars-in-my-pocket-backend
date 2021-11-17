package com.depromeet.threedollar.admin.service.faq.dto.request

import com.depromeet.threedollar.domain.domain.faq.FaqCategory
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UpdateFaqRequest(
    @field:NotBlank(message = "질문을 입력해주세요")
    val question: String = "",

    @field:NotBlank(message = "답변을 입력해주세요")
    val answer: String = "",

    @field:NotNull(message = "FAQ 카테고리를 입력해주세요")
    val category: FaqCategory?
)
