package com.depromeet.threedollar.admin.service.popup

import com.depromeet.threedollar.admin.service.popup.dto.request.AddPopupRequest
import com.depromeet.threedollar.admin.service.popup.dto.request.UpdatePopupRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.user.domain.popup.Popup
import com.depromeet.threedollar.domain.user.domain.popup.PopupRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PopupAdminService(
    private val popupRepository: PopupRepository
) {

    @Transactional
    fun addPopup(request: AddPopupRequest) {
        popupRepository.save(request.toEntity())
    }

    @Transactional
    fun updatePopup(popupId: Long, request: UpdatePopupRequest) {
        val popup = findPopupById(popupRepository, popupId)
        request.let {
            popup.update(it.positionType, it.platformType, it.imageUrl, it.linkUrl, it.priority, it.startDateTime, it.endDateTime)
        }
    }

    @Transactional
    fun deletePopup(popupId: Long) {
        val popup = findPopupById(popupRepository, popupId)
        popupRepository.delete(popup)
    }

}

fun findPopupById(popupRepository: PopupRepository, popupId: Long): Popup {
    return popupRepository.findByIdOrNull(popupId)
        ?: throw NotFoundException("해당하는 id (${popupId})을 가진 팝업은 존재하지 않습니다.")
}
