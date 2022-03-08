package com.depromeet.threedollar.api.user.service.visit;

import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.CONFLICT_VISIT_HISTORY;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class VisitHistoryServiceUtils {

    static void validateNotVisitedToday(VisitHistoryRepository visitHistoryRepository, Long storeId, Long userId, LocalDate date) {
        if (visitHistoryRepository.existsByStoreIdAndUserIdAndDateOfVisit(storeId, userId, date)) {
            throw new ConflictException(String.format("유저 (%s)는 (%s)에 (%s) 가게를 이미 방문 인증하였습니다", userId, date, storeId), CONFLICT_VISIT_HISTORY);
        }
    }

}