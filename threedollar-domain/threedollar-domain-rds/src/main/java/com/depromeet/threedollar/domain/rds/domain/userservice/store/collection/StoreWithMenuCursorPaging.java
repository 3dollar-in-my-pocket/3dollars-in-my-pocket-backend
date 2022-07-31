package com.depromeet.threedollar.domain.rds.domain.userservice.store.collection;

import com.depromeet.threedollar.domain.rds.core.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreWithMenuProjection;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class StoreWithMenuCursorPaging {

    private final CursorPagingSupporter<StoreWithMenuProjection> storesCursor;

    private StoreWithMenuCursorPaging(List<StoreWithMenuProjection> storesWithNextCursor, int size) {
        this.storesCursor = CursorPagingSupporter.of(storesWithNextCursor, size);
    }

    public static StoreWithMenuCursorPaging of(List<StoreWithMenuProjection> storeWithNextCursor, int size) {
        return new StoreWithMenuCursorPaging(storeWithNextCursor, size);
    }

    public boolean hasNext() {
        return storesCursor.hasNext();
    }

    public List<StoreWithMenuProjection> getCurrentCursorItems() {
        return storesCursor.getCurrentCursorItems();
    }

    @NotNull
    public StoreWithMenuProjection getNextCursor() {
        return storesCursor.getNextCursor();
    }

    public List<Long> getStoreIds() {
        return storesCursor.getCurrentCursorItems().stream()
            .map(StoreWithMenuProjection::getId)
            .collect(Collectors.toList());
    }

}
