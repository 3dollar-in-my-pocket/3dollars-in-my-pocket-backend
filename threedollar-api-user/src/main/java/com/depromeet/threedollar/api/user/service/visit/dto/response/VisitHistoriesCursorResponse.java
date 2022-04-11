package com.depromeet.threedollar.api.user.service.visit.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.depromeet.threedollar.domain.rds.common.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    public static VisitHistoriesCursorResponse of(CursorPagingSupporter<VisitHistory> visitHistoriesCursor) {
        List<VisitHistoryWithStoreResponse> visitHistories = convertToResponse(visitHistoriesCursor);
        if (visitHistoriesCursor.hasNext()) {
            return new VisitHistoriesCursorResponse(visitHistories, visitHistoriesCursor.getNextCursor().getId());
        }
        return new VisitHistoriesCursorResponse(visitHistories, LAST_CURSOR);
    }

    private static List<VisitHistoryWithStoreResponse> convertToResponse(CursorPagingSupporter<VisitHistory> visitHistoriesCursor) {
        return visitHistoriesCursor.getItemsInCurrentCursor().stream()
            .map(VisitHistoryWithStoreResponse::of)
            .collect(Collectors.toList());
    }

}
