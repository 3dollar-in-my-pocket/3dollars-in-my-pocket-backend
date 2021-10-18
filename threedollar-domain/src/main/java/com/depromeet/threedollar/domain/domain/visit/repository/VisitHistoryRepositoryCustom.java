package com.depromeet.threedollar.domain.domain.visit.repository;

import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;

import java.time.LocalDate;
import java.util.List;

public interface VisitHistoryRepositoryCustom {

    boolean existsByStoreIdAndUserIdAndDateOfVisit(Long storeId, Long userId, LocalDate dateOfVisit);

    List<VisitHistoryWithUserProjection> findVisitWithUserByStoreIdBetweenDate(Long storeId, LocalDate startDate, LocalDate endDate);

}
