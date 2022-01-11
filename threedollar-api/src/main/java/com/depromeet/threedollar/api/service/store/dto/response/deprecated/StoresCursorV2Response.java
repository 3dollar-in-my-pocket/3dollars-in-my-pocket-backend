package com.depromeet.threedollar.api.service.store.dto.response.deprecated;

import com.depromeet.threedollar.api.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
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

@Deprecated
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoresCursorV2Response {

    private static final long LAST_CURSOR = -1L;

    private List<StoreWithVisitsAndDistanceResponse> contents = new ArrayList<>();
    private long totalElements;
    private long nextCursor;

    private StoresCursorV2Response(List<StoreWithVisitsAndDistanceResponse> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static StoresCursorV2Response of(CursorSupporter<Store> storesCursor, VisitHistoryCounter visitHistoriesCounts,
                                            long totalElements, double latitude, double longitude) {
        if (storesCursor.isLastCursor()) {
            return StoresCursorV2Response.newLastCursor(storesCursor.getItemsInCurrentCursor(), visitHistoriesCounts, latitude, longitude, totalElements);
        }
        return StoresCursorV2Response.newCursorHasNext(storesCursor.getItemsInCurrentCursor(),
            visitHistoriesCounts, latitude, longitude, totalElements, storesCursor.getNextCursor().getId());
    }

    private static StoresCursorV2Response newLastCursor(List<Store> stores, VisitHistoryCounter collection,
                                                        double latitude, double longitude, long totalElements) {
        return newCursorHasNext(stores, collection, latitude, longitude, totalElements, LAST_CURSOR);
    }

    private static StoresCursorV2Response newCursorHasNext(List<Store> stores, VisitHistoryCounter collection,
                                                           double latitude, double longitude, long totalElements, long nextCursor) {
        return new StoresCursorV2Response(getContents(stores, collection, latitude, longitude), totalElements, nextCursor);
    }

    private static List<StoreWithVisitsAndDistanceResponse> getContents(List<Store> stores, VisitHistoryCounter collection, double latitude, double longitude) {
        return stores.stream()
            .map(store -> StoreWithVisitsAndDistanceResponse.of(store, latitude, longitude, collection))
            .collect(Collectors.toList());
    }

}
