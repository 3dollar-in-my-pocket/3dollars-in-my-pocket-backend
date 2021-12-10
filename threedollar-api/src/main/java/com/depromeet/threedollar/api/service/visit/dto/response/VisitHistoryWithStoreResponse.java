package com.depromeet.threedollar.api.service.visit.dto.response;

import com.depromeet.threedollar.api.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoryWithStoreResponse extends AuditingTimeResponse {

    private Long visitHistoryId;
    private VisitType type;
    private LocalDate dateOfVisit;

    private StoreInfoResponse store;

    @Builder(access = AccessLevel.PRIVATE)
    private VisitHistoryWithStoreResponse(Long visitHistoryId, VisitType type, LocalDate dateOfVisit, StoreInfoResponse store) {
        this.visitHistoryId = visitHistoryId;
        this.type = type;
        this.dateOfVisit = dateOfVisit;
        this.store = store;
    }

    public static VisitHistoryWithStoreResponse of(VisitHistory visitHistory) {
        VisitHistoryWithStoreResponse response = VisitHistoryWithStoreResponse.builder()
            .visitHistoryId(visitHistory.getId())
            .type(visitHistory.getType())
            .dateOfVisit(visitHistory.getDateOfVisit())
            .store(StoreInfoResponse.of(visitHistory.getStore()))
            .build();
        response.setBaseTime(visitHistory.getCreatedAt(), visitHistory.getUpdatedAt());
        return response;
    }

}
