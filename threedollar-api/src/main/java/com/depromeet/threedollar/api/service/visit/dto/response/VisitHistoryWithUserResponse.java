package com.depromeet.threedollar.api.service.visit.dto.response;

import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;
import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoryWithUserResponse extends AuditingTimeResponse {

    private Long visitHistoryId;

    private Long storeId;

    private VisitType type;

    private LocalDate dateOfVisit;

    private UserInfoResponse user;

    @Builder(access = AccessLevel.PRIVATE)
    private VisitHistoryWithUserResponse(Long visitHistoryId, Long storeId, VisitType type, LocalDate dateOfVisit, UserInfoResponse user) {
        this.visitHistoryId = visitHistoryId;
        this.storeId = storeId;
        this.type = type;
        this.dateOfVisit = dateOfVisit;
        this.user = user;
    }

    public static VisitHistoryWithUserResponse of(VisitHistoryWithUserProjection projection) {
        VisitHistoryWithUserResponse response = VisitHistoryWithUserResponse.builder()
            .visitHistoryId(projection.getVisitHistoryId())
            .storeId(projection.getStoreId())
            .type(projection.getType())
            .dateOfVisit(projection.getDateOfVisit())
            .user(UserInfoResponse.of(projection.getUserId(), projection.getUserName(), projection.getSocialType()))
            .build();
        response.setBaseTime(projection.getVisitCreatedAt(), projection.getVisitUpdatedAt());
        return response;
    }

}
