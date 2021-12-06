package com.depromeet.threedollar.domain.domain.visit.collection;

import com.depromeet.threedollar.domain.domain.visit.VisitType;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryCountProjection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VisitHistoriesCounter {

    private final Map<Long, Long> existCounter = new HashMap<>();
    private final Map<Long, Long> notExistCounter = new HashMap<>();

    private VisitHistoriesCounter(List<VisitHistoryCountProjection> visitHistoryWithCounts) {
        this.existCounter.putAll(visitHistoryWithCounts.stream()
            .filter(visit -> VisitType.EXISTS.equals(visit.getVisitType()))
            .collect(Collectors.toMap(VisitHistoryCountProjection::getStoreId, VisitHistoryCountProjection::getCounts)));
        this.notExistCounter.putAll(visitHistoryWithCounts.stream()
            .filter(visit -> VisitType.NOT_EXISTS.equals(visit.getVisitType()))
            .collect(Collectors.toMap(VisitHistoryCountProjection::getStoreId, VisitHistoryCountProjection::getCounts)));
    }

    public static VisitHistoriesCounter of(List<VisitHistoryCountProjection> visitHistoryWithCounts) {
        return new VisitHistoriesCounter(visitHistoryWithCounts);
    }

    public Long getStoreExistsVisitsCount(Long storeId) {
        return existCounter.getOrDefault(storeId, 0L);
    }

    public Long getStoreNotExistsVisitsCount(Long storeId) {
        return notExistCounter.getOrDefault(storeId, 0L);
    }

}
