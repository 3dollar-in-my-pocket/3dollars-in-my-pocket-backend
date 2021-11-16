package com.depromeet.threedollar.api.service.visit.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoryInfoResponse {

    private long existsCounts;

    private long notExistsCounts;

    private Boolean isCertified;

    private VisitHistoryInfoResponse(long existsCounts, long notExistsCounts) {
        this.existsCounts = existsCounts;
        this.notExistsCounts = notExistsCounts;
        this.isCertified = existsCounts > 0;
    }

    public static VisitHistoryInfoResponse of(long existsVisitsCount, long notExistsVisitsCount) {
        return new VisitHistoryInfoResponse(existsVisitsCount, notExistsVisitsCount);
    }

}
