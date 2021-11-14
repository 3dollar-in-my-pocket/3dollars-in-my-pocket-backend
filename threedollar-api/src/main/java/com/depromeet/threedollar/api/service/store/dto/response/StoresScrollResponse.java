package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.common.collection.ScrollPaginationCollection;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoriesCountCollection;
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
public class StoresScrollResponse {

    private static final long LAST_CURSOR = -1L;

    private List<StoreInfoResponse> contents = new ArrayList<>();
    private long totalElements;
    private long nextCursor;

    private StoresScrollResponse(List<StoreInfoResponse> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static StoresScrollResponse of(ScrollPaginationCollection<Store> scrollCollection, VisitHistoriesCountCollection visitHistoriesCounts,
                                          long totalElements, Double latitude, Double longitude) {
        if (scrollCollection.isLastScroll()) {
            return StoresScrollResponse.newLastScroll(scrollCollection.getItemsInCurrentScroll(), visitHistoriesCounts, latitude, longitude, totalElements);
        }
        return StoresScrollResponse.newScrollHasNext(scrollCollection.getItemsInCurrentScroll(),
            visitHistoriesCounts, latitude, longitude, totalElements, scrollCollection.getNextCursor().getId());
    }

    private static StoresScrollResponse newLastScroll(List<Store> stores, VisitHistoriesCountCollection collection,
                                                      Double latitude, Double longitude, long totalElements) {
        return newScrollHasNext(stores, collection, latitude, longitude, totalElements, LAST_CURSOR);
    }

    private static StoresScrollResponse newScrollHasNext(List<Store> stores, VisitHistoriesCountCollection collection,
                                                         Double latitude, Double longitude, long totalElements, long nextCursor) {
        return new StoresScrollResponse(getContents(stores, collection, latitude, longitude), totalElements, nextCursor);
    }

    private static List<StoreInfoResponse> getContents(List<Store> stores, VisitHistoriesCountCollection collection, Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return stores.stream()
                .map(store -> StoreInfoResponse.of(store, collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
                .collect(Collectors.toList());
        }
        return stores.stream()
            .map(store -> StoreInfoResponse.of(store, latitude, longitude,
                collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
            .collect(Collectors.toList());
    }

}
