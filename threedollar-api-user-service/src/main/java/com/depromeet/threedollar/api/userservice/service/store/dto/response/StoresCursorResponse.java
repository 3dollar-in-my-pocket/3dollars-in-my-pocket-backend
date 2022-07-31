package com.depromeet.threedollar.api.userservice.service.store.dto.response;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.collection.StoreWithMenuProjectionPagingCursor;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreWithMenuProjection;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.collection.VisitHistoryCounter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static StoresCursorResponse of(StoreWithMenuProjectionPagingCursor storesPagingCursor, VisitHistoryCounter visitHistoriesCounts, long totalElements) {
        List<StoreWithVisitCountsResponse> storesWithVisitCounts = combineStoreWithVisitsResponse(storesPagingCursor.getCurrentCursorItems(), visitHistoriesCounts);
        if (storesPagingCursor.hasNext()) {
            return new StoresCursorResponse(storesWithVisitCounts, totalElements, storesPagingCursor.getNextCursor().getId());
        }
        return new StoresCursorResponse(storesWithVisitCounts, totalElements, LAST_CURSOR);
    }

    private static List<StoreWithVisitCountsResponse> combineStoreWithVisitsResponse(List<StoreWithMenuProjection> stores, VisitHistoryCounter collection) {
        return stores.stream()
            .map(store -> StoreWithVisitCountsResponse.of(store, collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
            .collect(Collectors.toList());
    }

}
