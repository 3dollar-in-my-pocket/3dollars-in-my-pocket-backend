package com.depromeet.threedollar.api.userservice.service.visit.dto.response;

import com.depromeet.threedollar.domain.rds.core.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
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

    public static VisitHistoriesCursorResponse of(CursorPagingSupporter<VisitHistory> visitHistoriesCursor) {
        List<VisitHistoryWithStoreResponse> visitHistories = convertToResponse(visitHistoriesCursor);
        if (visitHistoriesCursor.hasNext()) {
            return new VisitHistoriesCursorResponse(visitHistories, visitHistoriesCursor.getNextCursor().getId());
        }
        return new VisitHistoriesCursorResponse(visitHistories, LAST_CURSOR);
    }

    private static List<VisitHistoryWithStoreResponse> convertToResponse(CursorPagingSupporter<VisitHistory> visitHistoriesCursor) {
        return visitHistoriesCursor.getCurrentCursorItems().stream()
            .map(VisitHistoryWithStoreResponse::of)
            .collect(Collectors.toList());
    }

}
