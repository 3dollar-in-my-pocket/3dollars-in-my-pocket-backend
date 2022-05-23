package com.depromeet.threedollar.api.vendor.service.visit;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.CONFLICT_VISIT_HISTORY;

import java.time.LocalDate;

import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.domain.rds.vendor.domain.visit.VisitHistoryRepository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class VisitHistoryServiceUtils {

    static void validateNotVisitedToday(VisitHistoryRepository visitHistoryRepository, Long storeId, Long userId, LocalDate date) {
        if (visitHistoryRepository.existsByStoreIdAndUserIdAndDateOfVisit(storeId, userId, date)) {
            throw new ConflictException(String.format("유저(%s)는 해당 날짜(%s)에 노점상(%s)를 이미 방문 인증하였습니다", userId, date, storeId), CONFLICT_VISIT_HISTORY);
        }
    }

}
