package com.depromeet.threedollar.api.userservice.service.visit.dto.response;

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.api.userservice.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.projection.VisitHistoryWithUserProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VisitHistoryWithUserResponse extends AuditingTimeResponse {

    private Long visitHistoryId;
    private VisitType type;
    private LocalDate dateOfVisit;

    private UserInfoResponse user;

    @Builder(access = AccessLevel.PRIVATE)
    private VisitHistoryWithUserResponse(Long visitHistoryId, VisitType type, LocalDate dateOfVisit, UserInfoResponse user) {
        this.visitHistoryId = visitHistoryId;
        this.type = type;
        this.dateOfVisit = dateOfVisit;
        this.user = user;
    }

    public static VisitHistoryWithUserResponse of(@NotNull VisitHistoryWithUserProjection projection, @Nullable User user) {
        VisitHistoryWithUserResponse response = VisitHistoryWithUserResponse.builder()
            .visitHistoryId(projection.getVisitHistoryId())
            .type(projection.getType())
            .dateOfVisit(projection.getDateOfVisit())
            .user(UserInfoResponse.of(user))
            .build();
        response.setAuditingTime(projection.getVisitCreatedAt(), projection.getVisitUpdatedAt());
        return response;
    }

}
