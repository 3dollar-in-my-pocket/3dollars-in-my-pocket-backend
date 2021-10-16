package com.depromeet.threedollar.application.mapper.menu

import com.depromeet.threedollar.application.mapper.menu.dto.response.MenuCategoryResponse
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType

object MenuCategoryMapper {

    fun retrieveMenuCategories(): List<MenuCategoryResponse> {
        return MenuCategoryType.values().asSequence()
            .filter { it.isViewed }
            .sortedBy { it.displayOrder }
            .map { MenuCategoryResponse.of(it) }
            .toList()
    }

}
