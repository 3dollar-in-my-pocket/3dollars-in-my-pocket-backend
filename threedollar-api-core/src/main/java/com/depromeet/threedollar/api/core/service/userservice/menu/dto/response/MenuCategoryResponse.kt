package com.depromeet.threedollar.api.core.service.userservice.menu.dto.response

import com.depromeet.threedollar.common.type.UserMenuCategoryType
import com.fasterxml.jackson.annotation.JsonProperty

data class MenuCategoryResponse(
    val category: UserMenuCategoryType,
    val name: String,
    val description: String,
    @get:JsonProperty("isNew")
    val isNew: Boolean,
) {

    companion object {
        @JvmStatic
        fun of(category: UserMenuCategoryType): MenuCategoryResponse {
            return MenuCategoryResponse(
                category = category,
                name = category.categoryName,
                description = category.description,
                isNew = category.isNew
            )
        }
    }

}
