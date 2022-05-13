package com.depromeet.threedollar.api.core.mapper.user.menu

import com.depromeet.threedollar.api.core.mapper.user.menu.dto.response.MenuCategoryResponse
import com.depromeet.threedollar.common.type.MenuCategoryType

object MenuCategoryMapper {

    fun retrieveActiveMenuCategories(): List<MenuCategoryResponse> {
        return MenuCategoryType.values().asSequence()
            .filter { it.isVisible }
            .sortedBy { it.displayOrder }
            .map { MenuCategoryResponse.of(it) }
            .toList()
    }

}
