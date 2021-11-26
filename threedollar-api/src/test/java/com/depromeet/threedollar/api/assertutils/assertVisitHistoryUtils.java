package com.depromeet.threedollar.api.assertutils;

import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryCountsResponse;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryWithStoreResponse;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryWithUserResponse;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.VisitType;

import java.time.LocalDate;

import static com.depromeet.threedollar.api.assertutils.assertStoreUtils.assertStoreInfoResponse;
import static com.depromeet.threedollar.api.assertutils.assertUserUtils.assertUserInfoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public final class assertVisitHistoryUtils {

    public static void assertVisitHistory(VisitHistory visitHistory, Long storeId, Long userId, VisitType type, LocalDate dateOfVisit) {
        assertAll(
            () -> assertThat(visitHistory.getStore().getId()).isEqualTo(storeId),
            () -> assertThat(visitHistory.getUserId()).isEqualTo(userId),
            () -> assertThat(visitHistory.getType()).isEqualTo(type),
            () -> assertThat(visitHistory.getDateOfVisit()).isEqualTo(dateOfVisit)
        );
    }

    public static void assertVisitHistoryInfoResponse(VisitHistoryCountsResponse visitHistory, int existsCount, int notExistsCount, boolean isCertified) {
        assertAll(
            () -> assertThat(visitHistory.getExistsCounts()).isEqualTo(existsCount),
            () -> assertThat(visitHistory.getNotExistsCounts()).isEqualTo(notExistsCount),
            () -> assertThat(visitHistory.getIsCertified()).isEqualTo(isCertified)
        );
    }

    public static void assertVisitHistoryWithUserResponse(VisitHistoryWithUserResponse response, VisitHistory visitHistory, Store store, User user) {
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
