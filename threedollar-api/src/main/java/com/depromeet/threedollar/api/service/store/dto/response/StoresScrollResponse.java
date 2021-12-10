package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.domain.collection.common.ScrollPaginationCollection;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.collection.visit.VisitHistoryCounter;
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

    private List<StoreWithVisitsResponse> contents = new ArrayList<>();
    private long totalElements;
    private long nextCursor;

    private StoresScrollResponse(List<StoreWithVisitsResponse> contents, long totalElements, long nextCursor) {
        this.contents = contents;
        this.totalElements = totalElements;
        this.nextCursor = nextCursor;
    }

    public static StoresScrollResponse of(ScrollPaginationCollection<Store> storesScroll, VisitHistoryCounter visitHistoriesCounts, long totalElements) {
        if (storesScroll.isLastScroll()) {
            return StoresScrollResponse.newLastScroll(storesScroll.getCurrentScrollItems(), visitHistoriesCounts, totalElements);
        }
        return StoresScrollResponse.newScrollHasNext(storesScroll.getCurrentScrollItems(),
            visitHistoriesCounts, totalElements, storesScroll.getNextCursor().getId());
    }

    private static StoresScrollResponse newLastScroll(List<Store> stores, VisitHistoryCounter collection, long totalElements) {
        return newScrollHasNext(stores, collection, totalElements, LAST_CURSOR);
    }

    private static StoresScrollResponse newScrollHasNext(List<Store> stores, VisitHistoryCounter collection, long totalElements, long nextCursor) {
        return new StoresScrollResponse(getContents(stores, collection), totalElements, nextCursor);
    }

    private static List<StoreWithVisitsResponse> getContents(List<Store> stores, VisitHistoryCounter collection) {
        return stores.stream()
            .map(store -> StoreWithVisitsResponse.of(store, collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
            .collect(Collectors.toList());
    }

}
