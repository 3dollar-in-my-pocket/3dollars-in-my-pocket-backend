package com.depromeet.threedollar.domain.rds.user.domain.visit.repository;

import com.depromeet.threedollar.domain.rds.user.domain.visit.projection.VisitHistoryCountProjection;
import com.depromeet.threedollar.domain.rds.user.domain.visit.projection.VisitHistoryWithUserProjection;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitType;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface VisitHistoryRepositoryCustom {

    boolean existsByStoreIdAndUserIdAndDateOfVisit(Long storeId, Long userId, LocalDate dateOfVisit);

    List<VisitHistoryWithUserProjection> findAllVisitWithUserByStoreIdAfterDate(Long storeId, LocalDate startDate);

    List<VisitHistory> findAllByUserIdUsingCursor(Long userId, Long lastHistoryId, int size);

    List<VisitHistoryCountProjection> findCountsByStoreIdWithGroup(List<Long> storeIds, LocalDate startDate);

	long findCountsByUserIdAndCategory(Long userId, @Nullable MenuCategoryType menuCategoryType);

    long findCountsByUserIdAndVisitType(Long userId, VisitType visitType);

}
