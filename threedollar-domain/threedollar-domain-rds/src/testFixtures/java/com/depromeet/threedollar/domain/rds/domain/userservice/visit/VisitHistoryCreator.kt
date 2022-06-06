package com.depromeet.threedollar.domain.rds.domain.userservice.visit

import java.time.LocalDate
import com.depromeet.threedollar.domain.rds.domain.TestFixture
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store

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
