package com.depromeet.threedollar.api.core.service.mapper.userservice.menu.dto.response

import com.depromeet.threedollar.common.type.UserMenuCategoryType
import com.fasterxml.jackson.annotation.JsonProperty

data class MenuCategoryResponse(
    @Deprecated(message = "v3.0.6부터 categoryId로 변경")
    val category: UserMenuCategoryType,
    val categoryId: UserMenuCategoryType,
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
                categoryId = category,
                name = category.categoryName,
                description = category.description,
                isNew = category.isNew
            )
        }
    }

}
