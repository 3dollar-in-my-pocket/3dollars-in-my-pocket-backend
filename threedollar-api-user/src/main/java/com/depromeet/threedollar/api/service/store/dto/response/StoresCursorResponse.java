package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.domain.common.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.user.domain.store.Store;
import com.depromeet.threedollar.domain.user.collection.visit.VisitHistoryCounter;
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

    public static StoresCursorResponse of(CursorPagingSupporter<Store> storesCursor, VisitHistoryCounter visitHistoriesCounts, long totalElements) {
        List<StoreWithVisitCountsResponse> storesWithVisitCounts = combineStoreWithVisitsResponse(storesCursor.getItemsInCurrentCursor(), visitHistoriesCounts);
        if (storesCursor.hasNext()) {
            return new StoresCursorResponse(storesWithVisitCounts, totalElements, storesCursor.getNextCursor().getId());
        }
        return new StoresCursorResponse(storesWithVisitCounts, totalElements, LAST_CURSOR);
    }

    private static List<StoreWithVisitCountsResponse> combineStoreWithVisitsResponse(List<Store> stores, VisitHistoryCounter collection) {
        return stores.stream()
            .map(store -> StoreWithVisitCountsResponse.of(store, collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
            .collect(Collectors.toList());
    }

}
