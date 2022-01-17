package com.depromeet.threedollar.application.service.displaymenu.dto.response

import com.depromeet.threedollar.domain.user.domain.menucategory.DisplayMenuCategory
import com.depromeet.threedollar.domain.user.domain.store.MenuCategoryType
import com.fasterxml.jackson.annotation.JsonProperty

data class DisplayMenuCategoryResponse(
    val category: MenuCategoryType,
    val name: String,
    val description: String,
    val iconUrl: String,
    @get:JsonProperty("isNew")
    val isNew: Boolean
) {

    companion object {
        @JvmStatic
        fun of(displayMenuCategory: DisplayMenuCategory): DisplayMenuCategoryResponse {
            return DisplayMenuCategoryResponse(
                category = displayMenuCategory.categoryType,
                name = displayMenuCategory.name,
                description = displayMenuCategory.description,
                iconUrl = displayMenuCategory.iconUrl,
                isNew = displayMenuCategory.isNew
            )
        }
    }

}
