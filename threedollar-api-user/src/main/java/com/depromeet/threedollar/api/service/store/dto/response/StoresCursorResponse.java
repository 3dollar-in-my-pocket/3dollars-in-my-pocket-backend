package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.domain.common.collection.CursorSupporter;
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

    private List<StoreWithVisitsResponse> contents = new ArrayList<>();
    private long totalElements;
    private long nextCursor;
    private boolean hasNext;

    private StoresCursorResponse(List<StoreWithVisitsResponse> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
        this.hasNext = LAST_CURSOR != nextCursor;
    }

    public static StoresCursorResponse of(CursorSupporter<Store> storesCursor, VisitHistoryCounter visitHistoriesCounts, long totalElements) {
        if (storesCursor.isLastCursor()) {
            return StoresCursorResponse.newLastCursor(storesCursor.getItemsInCurrentCursor(), visitHistoriesCounts, totalElements);
        }
        return StoresCursorResponse.newCursorHasNext(storesCursor.getItemsInCurrentCursor(),
            visitHistoriesCounts, totalElements, storesCursor.getNextCursor().getId());
    }

    private static StoresCursorResponse newLastCursor(List<Store> stores, VisitHistoryCounter collection, long totalElements) {
        return newCursorHasNext(stores, collection, totalElements, LAST_CURSOR);
    }

    private static StoresCursorResponse newCursorHasNext(List<Store> stores, VisitHistoryCounter collection, long totalElements, long nextCursor) {
        return new StoresCursorResponse(getContents(stores, collection), totalElements, nextCursor);
    }

    private static List<StoreWithVisitsResponse> getContents(List<Store> stores, VisitHistoryCounter collection) {
        return stores.stream()
            .map(store -> StoreWithVisitsResponse.of(store, collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
            .collect(Collectors.toList());
    }

}
