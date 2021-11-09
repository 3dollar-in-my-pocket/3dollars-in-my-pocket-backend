package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.domain.domain.visit.VisitHistoriesCountCollection;
import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoresScrollResponse {

    private List<StoreInfoResponse> contents = new ArrayList<>();
    private long totalElements;
    private long nextCursor;

    private StoresScrollResponse(List<StoreInfoResponse> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static StoresScrollResponse newLastScroll(List<Store> stores, VisitHistoriesCountCollection collection, long totalElements, Double latitude, Double longitude) {
        return newScrollHasNext(stores, collection, totalElements, latitude, longitude, -1L);
    }

    public static StoresScrollResponse newScrollHasNext(List<Store> stores, VisitHistoriesCountCollection collection, long totalElements, Double latitude, Double longitude, long nextCursor) {
        return new StoresScrollResponse(getContents(stores, collection, latitude, longitude), totalElements, nextCursor);
    }

    private static List<StoreInfoResponse> getContents(List<Store> stores, VisitHistoriesCountCollection collection, Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return stores.stream()
                .map(store -> StoreInfoResponse.of(store, collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
                .collect(Collectors.toList());
        }
        return stores.stream()
            .map(store -> StoreInfoResponse.of(store, latitude, longitude, collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
            .collect(Collectors.toList());
    }

}
