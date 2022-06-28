package com.depromeet.threedollar.api.core.mapper.userservice.menu

import com.depromeet.threedollar.common.type.UserMenuCategoryType

object MenuCategoryMapper {

    fun retrieveActiveMenuCategories(): List<MenuCategoryResponse> {
        return UserMenuCategoryType.values().asSequence()
            .filter { categoryType -> categoryType.isVisible }
            .sortedBy { categoryType -> categoryType.displayOrder }
            .map { categoryType -> MenuCategoryResponse.of(categoryType) }
            .toList()
    }

}