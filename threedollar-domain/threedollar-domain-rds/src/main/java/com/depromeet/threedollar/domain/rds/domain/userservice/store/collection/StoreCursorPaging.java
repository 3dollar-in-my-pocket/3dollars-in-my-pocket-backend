package com.depromeet.threedollar.domain.rds.domain.userservice.store.collection;

import com.depromeet.threedollar.domain.rds.core.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StoreCursorPaging {

    private final CursorPagingSupporter<Store> storesCursor;

    private StoreCursorPaging(List<Store> storesWithNextCursor, int size) {
        this.storesCursor = CursorPagingSupporter.of(storesWithNextCursor, size);
    }

    public static StoreCursorPaging of(List<Store> storesWithNextCursor, int size) {
        return new StoreCursorPaging(storesWithNextCursor, size);
    }

    public boolean hasNext() {
        return storesCursor.hasNext();
    }

    public List<Store> getCurrentCursorItems() {
        return storesCursor.getCurrentCursorItems();
    }

    @NotNull
    public Store getNextCursor() {
        return storesCursor.getNextCursor();
    }

}
