package com.depromeet.threedollar.application.service.displaymenu

import com.depromeet.threedollar.application.service.displaymenu.dto.response.DisplayMenuCategoryResponse
import com.depromeet.threedollar.domain.config.cache.CacheType.CacheKey.DISPLAY_MENU_CATGORIES
import com.depromeet.threedollar.domain.user.domain.menucategory.DisplayMenuCategoryRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DisplayMenuCategoryService(
    private val displayMenuCategoryRepository: DisplayMenuCategoryRepository
) {

    @Cacheable(key = "'ALL'", value = [DISPLAY_MENU_CATGORIES])
    @Transactional(readOnly = true)
    fun getDisplayMenuCategories(): List<DisplayMenuCategoryResponse> {
        return displayMenuCategoryRepository.findAll().asSequence()
            .filter { it.isVisible }
            .sortedBy { it.displayOrder }
            .map { DisplayMenuCategoryResponse.of(it) }
            .toList()
    }

}
