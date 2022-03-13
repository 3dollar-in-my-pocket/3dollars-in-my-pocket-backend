package com.depromeet.threedollar.api.core.mapper.user.menu.dto.response

import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCategoryType
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
            return MenuCategoryResponse(
                category = category,
                name = category.categoryName,
                description = category.description,
                isNew = category.isNew
            )
        }
    }

}
