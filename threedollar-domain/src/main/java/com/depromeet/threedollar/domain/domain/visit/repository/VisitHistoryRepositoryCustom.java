package com.depromeet.threedollar.domain.domain.visit.repository;

import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithCounts;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;

import java.time.LocalDate;
import java.util.List;

public interface VisitHistoryRepositoryCustom {

    boolean existsByStoreIdAndUserIdAndDateOfVisit(Long storeId, Long userId, LocalDate dateOfVisit);

    List<VisitHistoryWithUserProjection> findAllVisitWithUserByStoreIdBetweenDate(Long storeId, LocalDate startDate, LocalDate endDate);

    List<VisitHistory> findAllByUserIdWithScroll(Long userId, Long lastHistoryId, int size);

    long findCountsByUserId(Long userId);

    List<VisitHistoryWithCounts> findCountsByStoreIdWithGroup(List<Long> storeIds);

}
