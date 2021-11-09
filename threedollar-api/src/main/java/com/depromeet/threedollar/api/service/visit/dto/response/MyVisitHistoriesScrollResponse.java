package com.depromeet.threedollar.api.service.visit.dto.response;

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
public class MyVisitHistoriesScrollResponse {

    private List<VisitHistoryWithStoreResponse> contents = new ArrayList<>();

    private long nextCursor;

    private MyVisitHistoriesScrollResponse(List<VisitHistoryWithStoreResponse> contents, long nextCursor) {
        this.contents = contents;
        this.nextCursor = nextCursor;
    }

    public static MyVisitHistoriesScrollResponse newLastScroll(List<VisitHistory> visitHistories) {
        return newScrollHasNext(visitHistories, -1L);
    }

    public static MyVisitHistoriesScrollResponse newScrollHasNext(List<VisitHistory> visitHistories, long nextCursor) {
        return new MyVisitHistoriesScrollResponse(visitHistories.stream()
            .map(history -> VisitHistoryWithStoreResponse.of(history, history.getStore()))
            .collect(Collectors.toList()), nextCursor);
    }

}
