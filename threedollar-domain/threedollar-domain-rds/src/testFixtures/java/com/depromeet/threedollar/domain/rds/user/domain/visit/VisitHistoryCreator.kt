package com.depromeet.threedollar.domain.rds.user.domain.visit

import java.time.LocalDate
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType
import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

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
