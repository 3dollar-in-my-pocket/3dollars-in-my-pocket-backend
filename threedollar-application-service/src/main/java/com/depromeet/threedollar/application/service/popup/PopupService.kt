package com.depromeet.threedollar.application.service.popup

import com.depromeet.threedollar.application.config.cache.CacheType
import com.depromeet.threedollar.application.service.popup.dto.response.PopupResponse
import com.depromeet.threedollar.domain.domain.popup.PopupRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class PopupService(
    private val popupRepository: PopupRepository
) {

    @Cacheable(key = "'FIRST'", value = [CacheType.CacheKey.POPUP])
    @Transactional(readOnly = true)
    fun getActivatedPopups(): List<PopupResponse> {
        return popupRepository.findActivatedPopups(LocalDateTime.now())
            .map {
                PopupResponse.of(it)
            }
    }

}
