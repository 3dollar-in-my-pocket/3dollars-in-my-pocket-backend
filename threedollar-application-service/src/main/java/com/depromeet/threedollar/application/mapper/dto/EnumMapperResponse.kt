package com.depromeet.threedollar.application.mapper.dto

import com.depromeet.threedollar.domain.domain.faq.FaqCategory
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType
import com.fasterxml.jackson.annotation.JsonProperty


data class FaqCategoryResponse(
    val category: FaqCategory,
    val description: String,
    val displayOrder: Int
) {

    companion object {
        fun of(category: FaqCategory): FaqCategoryResponse {
            return FaqCategoryResponse(category, category.description, category.displayOrder)
        }
    }

}

data class MenuCategoryResponse(
    val category: MenuCategoryType,
    val name: String,
    @get:JsonProperty("isNew")
    val isNew: Boolean
) {

    companion object {
        fun of(category: MenuCategoryType): MenuCategoryResponse {
            return MenuCategoryResponse(category, category.name, category.isNew)
        }
    }

}
