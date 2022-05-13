package com.depromeet.threedollar.domain.rds.user.domain.visit.repository;

import java.time.LocalDate;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.common.type.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitType;
import com.depromeet.threedollar.domain.rds.user.domain.visit.projection.VisitHistoryCountProjection;
import com.depromeet.threedollar.domain.rds.user.domain.visit.projection.VisitHistoryWithUserProjection;

public interface VisitHistoryRepositoryCustom {

    boolean existsByStoreIdAndUserIdAndDateOfVisit(Long storeId, Long userId, LocalDate dateOfVisit);

    List<VisitHistoryWithUserProjection> findAllVisitWithUserByStoreIdAfterDate(Long storeId, LocalDate startDate);

    List<VisitHistory> findAllByUserIdUsingCursor(Long userId, Long lastHistoryId, int size);

    List<VisitHistoryCountProjection> countGroupingByStoreId(List<Long> storeIds, LocalDate startDate);

    long countByUserIdAndMenuCategoryType(Long userId, @Nullable MenuCategoryType menuCategoryType);

    long countByUserIdAndVisitType(Long userId, VisitType visitType);

}
