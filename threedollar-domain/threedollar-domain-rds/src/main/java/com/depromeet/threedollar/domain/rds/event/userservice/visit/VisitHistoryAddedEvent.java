package com.depromeet.threedollar.domain.rds.event.userservice.visit;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class VisitHistoryAddedEvent {

    private final Long visitHistoryId;

    private final Long userId;

    @Builder(access = AccessLevel.PRIVATE)
    private VisitHistoryAddedEvent(Long visitHistoryId, Long userId) {
        this.visitHistoryId = visitHistoryId;
        this.userId = userId;
    }

    public static VisitHistoryAddedEvent of(Long visitHistoryId, Long userId) {
        return VisitHistoryAddedEvent.builder()
            .visitHistoryId(visitHistoryId)
            .userId(userId)
            .build();
    }

}
