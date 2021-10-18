package com.depromeet.threedollar.api.service.visit.dto.response;

import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoryResponse extends AuditingTimeResponse {

    private Long visitId;

    private Long storeId;

    private VisitType type;

    private LocalDate dateOfVisit;

    private Long userId;

    private String userName;

    @Builder(access = AccessLevel.PRIVATE)
    private VisitHistoryResponse(Long visitId, Long storeId, VisitType type, LocalDate dateOfVisit, Long userId, String userName) {
        this.visitId = visitId;
        this.storeId = storeId;
        this.type = type;
        this.dateOfVisit = dateOfVisit;
        this.userId = userId;
        this.userName = userName;
    }

    public static VisitHistoryResponse of(VisitHistoryWithUserProjection projection) {
        VisitHistoryResponse response = VisitHistoryResponse.builder()
            .visitId(projection.getVisitId())
            .storeId(projection.getStoreId())
            .type(projection.getType())
            .dateOfVisit(projection.getDateOfVisit())
            .userId(projection.getUserId())
            .userName(projection.getUserName())
            .build();
        response.setBaseTime(projection.getVisitCreatedAt(), projection.getVisitUpdatedAt());
        return response;
    }

}
