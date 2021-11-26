package com.depromeet.threedollar.application.mapper.menu

import com.depromeet.threedollar.application.mapper.menu.dto.response.MenuCategoryResponse
import com.depromeet.threedollar.domain.domain.store.MenuCategoryType

object MenuCategoryMapper {

    fun retrieveActiveMenuCategories(): List<MenuCategoryResponse> {
        return MenuCategoryType.values().asSequence()
            .filter { it.isVisible }
            .sortedBy { it.displayOrder }
            .map { MenuCategoryResponse.of(it) }
            .toList()
    }

}
