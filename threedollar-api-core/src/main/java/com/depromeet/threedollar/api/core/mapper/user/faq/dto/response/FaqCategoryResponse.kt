package com.depromeet.threedollar.api.core.mapper.user.faq.dto.response

import com.depromeet.threedollar.domain.rds.vendor.domain.faq.FaqCategory

data class FaqCategoryResponse(
    val category: FaqCategory,
    val description: String,
    val displayOrder: Int,
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
