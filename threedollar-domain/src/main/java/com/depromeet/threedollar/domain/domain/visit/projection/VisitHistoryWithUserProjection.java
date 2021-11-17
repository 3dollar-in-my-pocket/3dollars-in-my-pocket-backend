package com.depromeet.threedollar.domain.domain.visit.projection;

import com.depromeet.threedollar.domain.domain.user.UserSocialType;
import com.depromeet.threedollar.domain.domain.visit.VisitType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
public class VisitHistoryWithUserProjection {

    private final Long visitHistoryId;
    private final Long storeId;
    private final VisitType type;
    private final LocalDate dateOfVisit;
    private final LocalDateTime visitCreatedAt;
    private final LocalDateTime visitUpdatedAt;

    private final Long userId;
    private final String userName;
    private final UserSocialType socialType;

    @QueryProjection
    public VisitHistoryWithUserProjection(Long visitHistoryId, Long storeId, VisitType type, LocalDate dateOfVisit,
                                          LocalDateTime visitCreatedAt, LocalDateTime visitUpdatedAt,
                                          Long userId, String userName, UserSocialType socialType) {
        this.visitHistoryId = visitHistoryId;
        this.storeId = storeId;
        this.type = type;
        this.dateOfVisit = dateOfVisit;
        this.visitCreatedAt = visitCreatedAt;
        this.visitUpdatedAt = visitUpdatedAt;
        this.userId = userId;
        this.userName = userName;
        this.socialType = socialType;
    }

}
