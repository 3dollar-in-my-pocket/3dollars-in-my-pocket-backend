package com.depromeet.threedollar.api.userservice.controller.visit.support;

import static com.depromeet.threedollar.api.userservice.controller.store.support.StoreAssertions.assertStoreInfoResponse;
import static com.depromeet.threedollar.api.userservice.controller.user.support.UserAssertions.assertUserInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.depromeet.threedollar.api.userservice.service.visit.dto.response.VisitHistoryCountsResponse;
import com.depromeet.threedollar.api.userservice.service.visit.dto.response.VisitHistoryWithStoreResponse;
import com.depromeet.threedollar.api.userservice.service.visit.dto.response.VisitHistoryWithUserResponse;
import com.depromeet.threedollar.domain.rds.domain.TestAssertions;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.User;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@TestAssertions
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VisitHistoryAssertions {

    public static void assertVisitHistoryInfoResponse(VisitHistoryCountsResponse visitHistory, int existsCount, int notExistsCount, boolean isCertified) {
        assertAll(
            () -> assertThat(visitHistory.getExistsCounts()).isEqualTo(existsCount),
            () -> assertThat(visitHistory.getNotExistsCounts()).isEqualTo(notExistsCount),
            () -> assertThat(visitHistory.getIsCertified()).isEqualTo(isCertified)
        );
    }

    public static void assertVisitHistoryWithUserResponse(VisitHistoryWithUserResponse response, VisitHistory visitHistory, User user) {
        assertAll(
            () -> assertThat(response.getVisitHistoryId()).isEqualTo(visitHistory.getId()),
            () -> assertThat(response.getType()).isEqualTo(visitHistory.getType()),
            () -> assertUserInfoResponse(response.getUser(), user.getId(), user.getName(), user.getSocialType())
        );
    }

    public static void assertVisitHistoryWithStoreResponse(VisitHistoryWithStoreResponse response, VisitHistory visitHistory, Store store) {
        assertAll(
            () -> assertThat(response.getVisitHistoryId()).isEqualTo(visitHistory.getId()),
            () -> assertThat(response.getDateOfVisit()).isEqualTo(visitHistory.getDateOfVisit()),
            () -> assertThat(response.getType()).isEqualTo(visitHistory.getType()),
            () -> assertStoreInfoResponse(response.getStore(), store)
        );
    }

}
