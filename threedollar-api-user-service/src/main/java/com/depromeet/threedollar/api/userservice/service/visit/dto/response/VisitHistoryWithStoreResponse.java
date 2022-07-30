package com.depromeet.threedollar.api.userservice.service.visit.dto.response;

import com.depromeet.threedollar.api.core.service.common.dto.response.AuditingTimeResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

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

    public static VisitHistoryWithStoreResponse of(@NotNull VisitHistory visitHistory) {
        VisitHistoryWithStoreResponse response = VisitHistoryWithStoreResponse.builder()
            .visitHistoryId(visitHistory.getId())
            .type(visitHistory.getType())
            .dateOfVisit(visitHistory.getDateOfVisit())
            .store(StoreInfoResponse.of(visitHistory.getStore()))
            .build();
        response.setAuditingTime(visitHistory.getCreatedAt(), visitHistory.getUpdatedAt());
        return response;
    }

}
