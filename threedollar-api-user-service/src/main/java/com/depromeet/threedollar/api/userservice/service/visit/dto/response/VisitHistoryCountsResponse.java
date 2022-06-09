package com.depromeet.threedollar.api.userservice.service.visit.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoryCountsResponse {

    private long existsCounts;
    private long notExistsCounts;
    private Boolean isCertified;

    private VisitHistoryCountsResponse(long existsCounts, long notExistsCounts) {
        this.existsCounts = existsCounts;
        this.notExistsCounts = notExistsCounts;
        this.isCertified = existsCounts > notExistsCounts;
    }

    public static VisitHistoryCountsResponse of(long existsVisitsCount, long notExistsVisitsCount) {
        return new VisitHistoryCountsResponse(existsVisitsCount, notExistsVisitsCount);
    }

}
