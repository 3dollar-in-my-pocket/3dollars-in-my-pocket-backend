package com.depromeet.threedollar.domain.user.domain.visit

import com.depromeet.threedollar.domain.user.domain.ObjectMother
import com.depromeet.threedollar.domain.user.domain.store.Store
import java.time.LocalDate

@ObjectMother
object VisitHistoryCreator {

    @JvmStatic
    fun create(
        store: Store,
        userId: Long,
        type: VisitType,
        dateOfVisit: LocalDate
    ): VisitHistory {
        return VisitHistory.builder()
            .store(store)
            .userId(userId)
            .type(type)
            .dateOfVisit(dateOfVisit)
            .build()
    }

}
