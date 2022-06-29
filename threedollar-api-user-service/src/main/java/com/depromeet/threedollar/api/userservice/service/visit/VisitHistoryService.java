package com.depromeet.threedollar.api.userservice.service.visit;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.CONFLICT_VISIT_HISTORY;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.threedollar.api.userservice.service.store.StoreServiceHelper;
import com.depromeet.threedollar.api.userservice.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.userservice.service.visit.dto.request.RetrieveMyVisitHistoriesRequest;
import com.depromeet.threedollar.api.userservice.service.visit.dto.response.VisitHistoriesCursorResponse;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.domain.rds.core.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VisitHistoryService {

    private final StoreRepository storeRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional
    public Long addVisitHistory(AddVisitHistoryRequest request, Long userId, LocalDate dateOfVisit) {
        Store store = StoreServiceHelper.findStoreById(storeRepository, request.getStoreId());
        validateNotVisitedToday(request.getStoreId(), userId, dateOfVisit);
        return visitHistoryRepository.save(request.toEntity(store, userId, dateOfVisit)).getId();
    }

    private void validateNotVisitedToday(Long storeId, Long userId, LocalDate date) {
        if (visitHistoryRepository.existsVisitHistoryByStoreIdAndUserIdAndDateOfVisit(storeId, userId, date)) {
            throw new ConflictException(String.format("유저(%s)는 해당 날짜(%s)에 유저 가게(%s)를 이미 방문 인증하였습니다", userId, date, storeId), CONFLICT_VISIT_HISTORY);
        }
    }

    @Transactional(readOnly = true)
    public VisitHistoriesCursorResponse retrieveMyVisitHistories(RetrieveMyVisitHistoriesRequest request, Long userId) {
        List<VisitHistory> visitHistoriesWithNextCursor = visitHistoryRepository.findAllByUserIdUsingCursor(userId, request.getCursor(), request.getSize() + 1);
        CursorPagingSupporter<VisitHistory> visitHistoriesCursor = CursorPagingSupporter.of(visitHistoriesWithNextCursor, request.getSize());
        return VisitHistoriesCursorResponse.of(visitHistoriesCursor);
    }

}
