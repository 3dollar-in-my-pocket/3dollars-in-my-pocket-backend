package com.depromeet.threedollar.domain.rds.domain.userservice.visit.collection;

import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.projection.VisitHistoryCountProjection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VisitHistoryCountDictionary {

    private final Map<Long, Long> existCounter = new HashMap<>();
    private final Map<Long, Long> notExistCounter = new HashMap<>();

    private VisitHistoryCountDictionary(List<VisitHistoryCountProjection> visitHistoryWithCounts) {
        this.existCounter.putAll(visitHistoryWithCounts.stream()
            .filter(visit -> VisitType.EXISTS.equals(visit.getVisitType()))
            .collect(Collectors.toMap(VisitHistoryCountProjection::getStoreId, VisitHistoryCountProjection::getCounts)));
        this.notExistCounter.putAll(visitHistoryWithCounts.stream()
            .filter(visit -> VisitType.NOT_EXISTS.equals(visit.getVisitType()))
            .collect(Collectors.toMap(VisitHistoryCountProjection::getStoreId, VisitHistoryCountProjection::getCounts)));
    }

    public static VisitHistoryCountDictionary of(List<VisitHistoryCountProjection> visitHistoryWithCounts) {
        return new VisitHistoryCountDictionary(visitHistoryWithCounts);
    }

    public long getStoreExistsVisitsCount(Long storeId) {
        return existCounter.getOrDefault(storeId, 0L);
    }

    public long getStoreNotExistsVisitsCount(Long storeId) {
        return notExistCounter.getOrDefault(storeId, 0L);
    }

}
