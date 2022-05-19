package com.depromeet.threedollar.domain.rds.user.domain.visit

import java.time.LocalDate
import com.depromeet.threedollar.domain.rds.user.domain.TestFixture
import com.depromeet.threedollar.domain.rds.user.domain.store.Store

@TestFixture
object VisitHistoryCreator {

    @JvmStatic
    fun create(
        store: Store,
        userId: Long,
        type: VisitType,
        dateOfVisit: LocalDate,
    ): VisitHistory {
        return VisitHistory.builder()
            .store(store)
            .userId(userId)
            .type(type)
            .dateOfVisit(dateOfVisit)
            .build()
    }

}
