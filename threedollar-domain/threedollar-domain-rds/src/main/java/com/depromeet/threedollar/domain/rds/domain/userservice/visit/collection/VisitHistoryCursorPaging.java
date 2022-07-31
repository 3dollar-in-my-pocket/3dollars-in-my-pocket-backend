package com.depromeet.threedollar.domain.rds.domain.userservice.visit.collection;

import com.depromeet.threedollar.domain.rds.core.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VisitHistoryCursorPaging {

    private final CursorPagingSupporter<VisitHistory> visitHistoriesCursor;

    private VisitHistoryCursorPaging(List<VisitHistory> visitHistoriesWithNextCursor, int size) {
        this.visitHistoriesCursor = CursorPagingSupporter.of(visitHistoriesWithNextCursor, size);
    }

    public static VisitHistoryCursorPaging of(List<VisitHistory> visitHistoriesWithNextCursor, int size) {
        return new VisitHistoryCursorPaging(visitHistoriesWithNextCursor, size);
    }

    public boolean hasNext() {
        return visitHistoriesCursor.hasNext();
    }

    public List<VisitHistory> getCurrentCursorItems() {
        return visitHistoriesCursor.getCurrentCursorItems();
    }

    @NotNull
    public VisitHistory getNextCursor() {
        return visitHistoriesCursor.getNextCursor();
    }

}
