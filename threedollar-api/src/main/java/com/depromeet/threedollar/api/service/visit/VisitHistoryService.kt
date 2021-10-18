package com.depromeet.threedollar.api.service.visit

import com.depromeet.threedollar.api.service.store.StoreServiceUtils
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.domain.domain.store.StoreRepository
import com.depromeet.threedollar.domain.domain.visit.VisitHistory
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class VisitHistoryService(
    private val storeRepository: StoreRepository,
    private val visitHistoryRepository: VisitHistoryRepository
) {

    @Transactional
    fun addVisitHistory(request: AddVisitHistoryRequest, userId: Long) {
        StoreServiceUtils.validateExistsStore(storeRepository, request.storeId)
        val today = LocalDate.now()
        validateNotVisitedToday(visitHistoryRepository, request.storeId, userId, today)
        visitHistoryRepository.save<VisitHistory>(request.toEntity(userId, today))
    }

}

fun validateNotVisitedToday(
    visitHistoryRepository: VisitHistoryRepository,
    storeId: Long?,
    userId: Long?,
    today: LocalDate?
) {
    if (visitHistoryRepository.existsByStoreIdAndUserIdAndDateOfVisit(storeId, userId, today)) {
        throw ConflictException(String.format("유저 (%s)는 (%s)에 (%s) 가게를 이미 방문 인증하였습니다", userId, today, storeId))
    }
}
