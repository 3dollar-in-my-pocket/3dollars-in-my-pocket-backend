package com.depromeet.threedollar.domain.domain.visit.projection;

import com.depromeet.threedollar.domain.domain.visit.VisitType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class VisitHistoryCountProjection {

    private final Long storeId;

    private final VisitType visitType;

    private final long counts;

    @QueryProjection
    public VisitHistoryCountProjection(Long storeId, VisitType visitType, long counts) {
        this.storeId = storeId;
        this.visitType = visitType;
        this.counts = counts;
    }

}
