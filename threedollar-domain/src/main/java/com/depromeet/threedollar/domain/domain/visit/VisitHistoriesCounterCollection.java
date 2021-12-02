package com.depromeet.threedollar.domain.domain.visit;

import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryCountProjection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VisitHistoriesCounterCollection {

    private final Map<Long, Long> existCounter = new HashMap<>();
    private final Map<Long, Long> notExistCounter = new HashMap<>();

    private VisitHistoriesCounterCollection(List<VisitHistoryCountProjection> visitHistoryWithCounts) {
        this.existCounter.putAll(visitHistoryWithCounts.stream()
            .filter(visit -> visit.getVisitType().equals(VisitType.EXISTS))
            .collect(Collectors.toMap(VisitHistoryCountProjection::getStoreId, VisitHistoryCountProjection::getCounts)));
        this.notExistCounter.putAll(visitHistoryWithCounts.stream()
            .filter(visit -> visit.getVisitType().equals(VisitType.NOT_EXISTS))
            .collect(Collectors.toMap(VisitHistoryCountProjection::getStoreId, VisitHistoryCountProjection::getCounts)));
    }

    public static VisitHistoriesCounterCollection of(List<VisitHistoryCountProjection> visitHistoryWithCounts) {
        return new VisitHistoriesCounterCollection(visitHistoryWithCounts);
    }

    public Long getStoreExistsVisitsCount(Long storeId) {
        return existCounter.getOrDefault(storeId, 0L);
    }

    public Long getStoreNotExistsVisitsCount(Long storeId) {
        return notExistCounter.getOrDefault(storeId, 0L);
    }

}
