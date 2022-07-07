package com.depromeet.threedollar.domain.rds.domain.userservice.visit.projection;

import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserSocialType;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    private final Long userId;

    @Nullable
    private final String userName;

    @Nullable
    private final UserSocialType socialType;

    @QueryProjection
    public VisitHistoryWithUserProjection(VisitHistory visitHistory, User user) {
        this.visitHistoryId = visitHistory.getId();
        this.storeId = visitHistory.getStore().getId();
        this.type = visitHistory.getType();
        this.dateOfVisit = visitHistory.getDateOfVisit();
        this.visitCreatedAt = visitHistory.getCreatedAt();
        this.visitUpdatedAt = visitHistory.getUpdatedAt();
        this.userId = user.getId();
        this.userName = user.getName();
        this.socialType = user.getSocialType();
    }

}
