package com.depromeet.threedollar.domain.domain.visit;

import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithCounts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoriesCountCollection {

    private final Map<Long, Long> existsVisitsCount = new HashMap<>();
    private final Map<Long, Long> notExistsVisitsCount = new HashMap<>();

    private VisitHistoriesCountCollection(List<VisitHistoryWithCounts> visitHistoryWithCounts) {
        this.existsVisitsCount.putAll(visitHistoryWithCounts.stream()
            .filter(visit -> visit.getVisitType().equals(VisitType.EXISTS))
            .collect(Collectors.toMap(VisitHistoryWithCounts::getStoreId, VisitHistoryWithCounts::getHistoriesCount)));
        this.notExistsVisitsCount.putAll(visitHistoryWithCounts.stream()
            .filter(visit -> visit.getVisitType().equals(VisitType.NOT_EXISTS))
            .collect(Collectors.toMap(VisitHistoryWithCounts::getStoreId, VisitHistoryWithCounts::getHistoriesCount)));
    }

    public static VisitHistoriesCountCollection of(List<VisitHistoryWithCounts> visitHistoryWithCounts) {
        return new VisitHistoriesCountCollection(visitHistoryWithCounts);
    }

    public Long getStoreExistsVisitsCount(Long storeId) {
        return existsVisitsCount.getOrDefault(storeId, 0L);
    }

    public Long getStoreNotExistsVisitsCount(Long storeId) {
        return notExistsVisitsCount.getOrDefault(storeId, 0L);
    }

}
