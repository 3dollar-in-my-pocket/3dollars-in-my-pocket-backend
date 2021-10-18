package com.depromeet.threedollar.domain.domain.visit.repository;

import java.time.LocalDate;

public interface VisitHistoryRepositoryCustom {

    boolean existsByStoreIdAndUserIdAndDateOfVisit(Long storeId, Long userId, LocalDate dateOfVisit);

}
