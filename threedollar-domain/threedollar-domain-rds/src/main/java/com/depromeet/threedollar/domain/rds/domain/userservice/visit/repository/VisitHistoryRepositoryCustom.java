package com.depromeet.threedollar.domain.rds.domain.userservice.visit.repository;

import java.time.LocalDate;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.projection.VisitHistoryCountProjection;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.projection.VisitHistoryWithUserProjection;

public interface VisitHistoryRepositoryCustom {

    boolean existsVisitHistoryByStoreIdAndUserIdAndDateOfVisit(Long storeId, Long userId, LocalDate dateOfVisit);

    List<VisitHistoryWithUserProjection> findAllVisitWithUserByStoreIdAfterDate(Long storeId, LocalDate startDate);

    List<VisitHistory> findAllByUserIdUsingCursor(Long userId, @Nullable Long lastHistoryId, int size);

    List<VisitHistoryCountProjection> countGroupingByStoreId(List<Long> storeIds, LocalDate startDate);

    long countByUserIdAndMenuCategoryType(Long userId, @Nullable UserMenuCategoryType menuCategoryType);

    long countByUserIdAndVisitType(Long userId, VisitType visitType);

}
