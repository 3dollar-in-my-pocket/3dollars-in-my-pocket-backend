package com.depromeet.threedollar.api.service.visit.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoryInfoResponse {

    private long existsVisitsCount;

    private long notExistsVisitsCount;

    private VisitHistoryInfoResponse(long existsVisitsCount, long notExistsVisitsCount) {
        this.existsVisitsCount = existsVisitsCount;
        this.notExistsVisitsCount = notExistsVisitsCount;
    }

    public static VisitHistoryInfoResponse of(long existsVisitsCount, long notExistsVisitsCount) {
        return new VisitHistoryInfoResponse(existsVisitsCount, notExistsVisitsCount);
    }

}
