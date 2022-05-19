package com.depromeet.threedollar.api.core.mapper.user.menu

import com.depromeet.threedollar.api.core.mapper.user.menu.dto.response.MenuCategoryResponse
import com.depromeet.threedollar.common.type.UserMenuCategoryType

object MenuCategoryMapper {

    fun retrieveActiveMenuCategories(): List<MenuCategoryResponse> {
        return UserMenuCategoryType.values().asSequence()
            .filter { it.isVisible }
            .sortedBy { it.displayOrder }
            .map { MenuCategoryResponse.of(it) }
            .toList()
    }

}
