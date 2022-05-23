package com.depromeet.threedollar.domain.rds.vendor.collection.visit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.depromeet.threedollar.domain.rds.vendor.domain.visit.VisitType;
import com.depromeet.threedollar.domain.rds.vendor.domain.visit.projection.VisitHistoryCountProjection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VisitHistoryCounter {

    private final Map<Long, Long> existCounter = new HashMap<>();
    private final Map<Long, Long> notExistCounter = new HashMap<>();

    private VisitHistoryCounter(List<VisitHistoryCountProjection> visitHistoryWithCounts) {
        this.existCounter.putAll(visitHistoryWithCounts.stream()
            .filter(visit -> VisitType.EXISTS.equals(visit.getVisitType()))
            .collect(Collectors.toMap(VisitHistoryCountProjection::getStoreId, VisitHistoryCountProjection::getCounts)));
        this.notExistCounter.putAll(visitHistoryWithCounts.stream()
            .filter(visit -> VisitType.NOT_EXISTS.equals(visit.getVisitType()))
            .collect(Collectors.toMap(VisitHistoryCountProjection::getStoreId, VisitHistoryCountProjection::getCounts)));
    }

    public static VisitHistoryCounter of(List<VisitHistoryCountProjection> visitHistoryWithCounts) {
        return new VisitHistoryCounter(visitHistoryWithCounts);
    }

    public long getStoreExistsVisitsCount(Long storeId) {
        return existCounter.getOrDefault(storeId, 0L);
    }

    public long getStoreNotExistsVisitsCount(Long storeId) {
        return notExistCounter.getOrDefault(storeId, 0L);
    }

}
