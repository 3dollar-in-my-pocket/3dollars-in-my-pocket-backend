package com.depromeet.threedollar.domain.domain.visit.repository;

import com.depromeet.threedollar.domain.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryCountProjection;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface VisitHistoryRepositoryCustom {

    boolean existsByStoreIdAndUserIdAndDateOfVisit(Long storeId, Long userId, LocalDate dateOfVisit);

    List<VisitHistoryWithUserProjection> findAllVisitWithUserByStoreIdAfterDate(Long storeId, LocalDate startDate);

    List<VisitHistory> findAllByUserIdWithScroll(Long userId, Long lastHistoryId, int size);

    List<VisitHistoryCountProjection> findCountsByStoreIdWithGroup(List<Long> storeIds, LocalDate startDate);

	long findCountsByUserIdAndCategory(Long userId, @Nullable MenuCategoryType menuCategoryType);

    long findCountsByUserIdAndVisitType(Long userId, VisitType visitType);

}
