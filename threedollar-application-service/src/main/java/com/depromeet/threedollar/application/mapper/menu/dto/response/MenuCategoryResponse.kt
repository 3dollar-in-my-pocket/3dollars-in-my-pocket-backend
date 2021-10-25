package com.depromeet.threedollar.application.mapper.menu.dto.response

import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType
import com.fasterxml.jackson.annotation.JsonProperty

data class MenuCategoryResponse(
    val category: MenuCategoryType,
    val name: String,
    val description: String,
    @get:JsonProperty("isNew")
    val isNew: Boolean
) {

    companion object {
        @JvmStatic
        fun of(category: MenuCategoryType): MenuCategoryResponse {
            return MenuCategoryResponse(category, category.categoryName, category.description, category.isNew)
        }
    }

}
