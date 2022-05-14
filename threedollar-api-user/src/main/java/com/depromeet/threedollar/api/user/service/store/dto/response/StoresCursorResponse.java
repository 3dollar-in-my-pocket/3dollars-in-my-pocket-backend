package com.depromeet.threedollar.api.user.service.store.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.depromeet.threedollar.domain.rds.common.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.user.collection.visit.VisitHistoryCounter;
import com.depromeet.threedollar.domain.rds.user.domain.store.projection.StoreWithMenuProjection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoresCursorResponse {

    private static final long LAST_CURSOR = -1L;

    private List<StoreWithVisitCountsResponse> contents = new ArrayList<>();
    private long totalElements;
    private long nextCursor;
    private boolean hasNext;

    private StoresCursorResponse(List<StoreWithVisitCountsResponse> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
        this.hasNext = LAST_CURSOR != nextCursor;
    }

    public static StoresCursorResponse of(CursorPagingSupporter<StoreWithMenuProjection> storesCursor, VisitHistoryCounter visitHistoriesCounts, long totalElements) {
        List<StoreWithVisitCountsResponse> storesWithVisitCounts = combineStoreWithVisitsResponse(storesCursor.getCurrentCursorItems(), visitHistoriesCounts);
        if (storesCursor.hasNext()) {
            return new StoresCursorResponse(storesWithVisitCounts, totalElements, storesCursor.getNextCursor().getId());
        }
        return new StoresCursorResponse(storesWithVisitCounts, totalElements, LAST_CURSOR);
    }

    private static List<StoreWithVisitCountsResponse> combineStoreWithVisitsResponse(List<StoreWithMenuProjection> stores, VisitHistoryCounter collection) {
        return stores.stream()
            .map(store -> StoreWithVisitCountsResponse.of(store, collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
            .collect(Collectors.toList());
    }

}
