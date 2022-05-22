package com.depromeet.threedollar.api.user.service.visit;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.threedollar.api.user.service.store.StoreServiceUtils;
import com.depromeet.threedollar.api.user.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.user.service.visit.dto.request.RetrieveMyVisitHistoriesRequest;
import com.depromeet.threedollar.api.user.service.visit.dto.response.VisitHistoriesCursorResponse;
import com.depromeet.threedollar.domain.rds.common.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VisitHistoryService {

    private final StoreRepository storeRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional
    public Long addVisitHistory(AddVisitHistoryRequest request, Long userId, LocalDate dateOfVisit) {
        Store store = StoreServiceUtils.findStoreById(storeRepository, request.getStoreId());
        VisitHistoryServiceUtils.validateNotVisitedToday(visitHistoryRepository, request.getStoreId(), userId, dateOfVisit);
        return visitHistoryRepository.save(request.toEntity(store, userId, dateOfVisit)).getId();
    }

    @Transactional(readOnly = true)
    public VisitHistoriesCursorResponse retrieveMyVisitHistories(RetrieveMyVisitHistoriesRequest request, Long userId) {
        List<VisitHistory> visitHistoriesWithNextCursor = visitHistoryRepository.findAllByUserIdUsingCursor(userId, request.getCursor(), request.getSize() + 1);
        CursorPagingSupporter<VisitHistory> visitHistoriesCursor = CursorPagingSupporter.of(visitHistoriesWithNextCursor, request.getSize());
        return VisitHistoriesCursorResponse.of(visitHistoriesCursor);
    }

}
