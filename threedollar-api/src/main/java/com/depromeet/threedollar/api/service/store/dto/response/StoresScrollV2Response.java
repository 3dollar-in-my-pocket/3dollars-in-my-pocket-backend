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

@Deprecated
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoresScrollV2Response {

    private static final long LAST_CURSOR = -1L;

    private List<StoreWithVisitsAndDistanceResponse> contents = new ArrayList<>();
    private long totalElements;
    private long nextCursor;

    private StoresScrollV2Response(List<StoreWithVisitsAndDistanceResponse> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static StoresScrollV2Response of(ScrollPaginationCollection<Store> scrollCollection, VisitHistoriesCountCollection visitHistoriesCounts,
                                            long totalElements, Double latitude, Double longitude) {
        if (scrollCollection.isLastScroll()) {
            return StoresScrollV2Response.newLastScroll(scrollCollection.getItemsInCurrentScroll(), visitHistoriesCounts, latitude, longitude, totalElements);
        }
        return StoresScrollV2Response.newScrollHasNext(scrollCollection.getItemsInCurrentScroll(),
            visitHistoriesCounts, latitude, longitude, totalElements, scrollCollection.getNextCursor().getId());
    }

    private static StoresScrollV2Response newLastScroll(List<Store> stores, VisitHistoriesCountCollection collection,
                                                        Double latitude, Double longitude, long totalElements) {
        return newScrollHasNext(stores, collection, latitude, longitude, totalElements, LAST_CURSOR);
    }

    private static StoresScrollV2Response newScrollHasNext(List<Store> stores, VisitHistoriesCountCollection collection,
                                                           Double latitude, Double longitude, long totalElements, long nextCursor) {
        return new StoresScrollV2Response(getContents(stores, collection, latitude, longitude), totalElements, nextCursor);
    }

    private static List<StoreWithVisitsAndDistanceResponse> getContents(List<Store> stores, VisitHistoriesCountCollection collection, Double latitude, Double longitude) {
        return stores.stream()
            .map(store -> StoreWithVisitsAndDistanceResponse.of(store, latitude, longitude,
                collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
            .collect(Collectors.toList());
    }

}
