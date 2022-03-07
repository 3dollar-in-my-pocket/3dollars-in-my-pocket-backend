package com.depromeet.threedollar.domain.rds.user.event.visit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoryAddedEvent {

    private final Long visitHistoryId;

    private final Long userId;

    public static VisitHistoryAddedEvent of(Long visitHistoryId, Long userId) {
        return new VisitHistoryAddedEvent(visitHistoryId, userId);
    }

}
