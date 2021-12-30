package com.depromeet.threedollar.application.service.popup

import com.depromeet.threedollar.application.service.popup.dto.request.GetActivatedPopupsRequest
import com.depromeet.threedollar.application.service.popup.dto.response.PopupResponse
import com.depromeet.threedollar.domain.config.cache.CacheType.CacheKey.POPUP
import com.depromeet.threedollar.domain.domain.popup.PopupRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class PopupService(
    private val popupRepository: PopupRepository
) {

    @Cacheable(key = "#request.platform", value = [POPUP])
    @Transactional(readOnly = true)
    fun getActivatedPopups(request: GetActivatedPopupsRequest): List<PopupResponse> {
        return popupRepository.findActivatedPopupsByPlatform(request.position, request.platform, LocalDateTime.now())
            .map { PopupResponse.of(it) }
    }

}
