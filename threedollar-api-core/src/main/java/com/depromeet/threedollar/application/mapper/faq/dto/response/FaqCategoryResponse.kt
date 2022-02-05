package com.depromeet.threedollar.application.mapper.faq.dto.response

import com.depromeet.threedollar.domain.user.domain.faq.FaqCategory

data class FaqCategoryResponse(
    val category: FaqCategory,
    val description: String,
    val displayOrder: Int
) {

    companion object {
        fun of(category: FaqCategory): FaqCategoryResponse {
            return FaqCategoryResponse(
                category = category,
                description = category.description,
                displayOrder = category.displayOrder
            )
        }
    }

}
