package com.depromeet.threedollar.api.service.visit.dto.response;

import com.depromeet.threedollar.domain.collection.common.ScrollPaginationCollection;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
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
public class VisitHistoriesScrollResponse {

    private static final long LAST_CURSOR = -1L;

    private List<VisitHistoryWithStoreResponse> contents = new ArrayList<>();
    private long nextCursor;

    private VisitHistoriesScrollResponse(List<VisitHistoryWithStoreResponse> contents, long nextCursor) {
        this.contents = contents;
        this.nextCursor = nextCursor;
    }

    public static VisitHistoriesScrollResponse of(ScrollPaginationCollection<VisitHistory> visitHistoriesScroll) {
        if (visitHistoriesScroll.isLastScroll()) {
            return newLastScroll(visitHistoriesScroll.getCurrentScrollItems());
        }
        return newScrollHasNext(visitHistoriesScroll.getCurrentScrollItems(), visitHistoriesScroll.getNextCursor().getId());
    }

    private static VisitHistoriesScrollResponse newLastScroll(List<VisitHistory> visitHistories) {
        return newScrollHasNext(visitHistories, LAST_CURSOR);
    }

    private static VisitHistoriesScrollResponse newScrollHasNext(List<VisitHistory> visitHistories, long nextCursor) {
        return new VisitHistoriesScrollResponse(visitHistories.stream()
            .map(VisitHistoryWithStoreResponse::of)
            .collect(Collectors.toList()), nextCursor);
    }

}
