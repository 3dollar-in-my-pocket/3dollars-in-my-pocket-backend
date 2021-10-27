package com.depromeet.threedollar.api.service.visit.dto.response;

import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoryWithUserResponse extends AuditingTimeResponse {

    private Long visitHistoryId;

    private Long storeId;

    private VisitType type;

    private UserInfoResponse user;

    @Builder(access = AccessLevel.PRIVATE)
    private VisitHistoryWithUserResponse(Long visitHistoryId, Long storeId, VisitType type, UserInfoResponse user) {
        this.visitHistoryId = visitHistoryId;
        this.storeId = storeId;
        this.type = type;
        this.user = user;
    }

    public static VisitHistoryWithUserResponse of(VisitHistoryWithUserProjection projection) {
        VisitHistoryWithUserResponse response = VisitHistoryWithUserResponse.builder()
            .visitHistoryId(projection.getVisitHistoryId())
            .storeId(projection.getStoreId())
            .type(projection.getType())
            .user(UserInfoResponse.of(projection.getUserId(), projection.getUserName(), projection.getSocialType()))
            .build();
        response.setBaseTime(projection.getVisitCreatedAt(), projection.getVisitUpdatedAt());
        return response;
    }

}
