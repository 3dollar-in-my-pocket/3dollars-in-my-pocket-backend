package com.depromeet.threedollar.api.service.visit.dto.response;

import com.depromeet.threedollar.domain.common.collection.CursorSupporter;
import com.depromeet.threedollar.domain.user.domain.visit.VisitHistory;
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
public class VisitHistoriesCursorResponse {

    private static final long LAST_CURSOR = -1L;

    private List<VisitHistoryWithStoreResponse> contents = new ArrayList<>();
    private long nextCursor;
    private boolean hasNext;

    private VisitHistoriesCursorResponse(List<VisitHistoryWithStoreResponse> contents, long nextCursor) {
        this.contents = contents;
        this.nextCursor = nextCursor;
        this.hasNext = LAST_CURSOR != nextCursor;
    }

    public static VisitHistoriesCursorResponse of(CursorSupporter<VisitHistory> visitHistoriesCursor) {
        if (visitHistoriesCursor.isLastCursor()) {
            return newLastCursor(visitHistoriesCursor.getItemsInCurrentCursor());
        }
        return newCursorHasNext(visitHistoriesCursor.getItemsInCurrentCursor(), visitHistoriesCursor.getNextCursor().getId());
    }

    private static VisitHistoriesCursorResponse newLastCursor(List<VisitHistory> visitHistories) {
        return newCursorHasNext(visitHistories, LAST_CURSOR);
    }

    private static VisitHistoriesCursorResponse newCursorHasNext(List<VisitHistory> visitHistories, long nextCursor) {
        return new VisitHistoriesCursorResponse(visitHistories.stream()
            .map(VisitHistoryWithStoreResponse::of)
            .collect(Collectors.toList()), nextCursor);
    }

}
