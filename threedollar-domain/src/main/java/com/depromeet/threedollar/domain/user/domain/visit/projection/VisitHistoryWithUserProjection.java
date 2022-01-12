package com.depromeet.threedollar.domain.user.domain.visit.projection;

import com.depromeet.threedollar.domain.user.domain.user.UserSocialType;
import com.depromeet.threedollar.domain.user.domain.visit.VisitType;
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
    public VisitHistoryWithUserProjection(Long visitHistoryId, Long storeId, VisitType type, LocalDate dateOfVisit,
                                          LocalDateTime visitCreatedAt, LocalDateTime visitUpdatedAt,
                                          @Nullable Long userId, @Nullable String userName, @Nullable UserSocialType socialType) {
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
