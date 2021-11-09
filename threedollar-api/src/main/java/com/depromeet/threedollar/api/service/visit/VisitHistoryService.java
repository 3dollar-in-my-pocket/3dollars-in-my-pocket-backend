package com.depromeet.threedollar.api.service.visit;

import com.depromeet.threedollar.api.service.store.StoreServiceUtils;
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.request.RetrieveMyVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.response.MyVisitHistoriesScrollResponse;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VisitHistoryService {

    private final StoreRepository storeRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional
    public void addVisitHistory(AddVisitHistoryRequest request, Long userId) {
        final LocalDate today = LocalDate.now();
        Store store = StoreServiceUtils.findStoreById(storeRepository, request.getStoreId());
        VisitHistoryServiceUtils.validateNotVisitedToday(visitHistoryRepository, request.getStoreId(), userId, today);
        visitHistoryRepository.save(request.toEntity(store, userId, today));
    }

    @Transactional(readOnly = true)
    public MyVisitHistoriesScrollResponse retrieveMyVisitHistories(RetrieveMyVisitHistoryRequest request, Long userId) {
        List<VisitHistory> currentAndNextHistories = visitHistoryRepository.findAllByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1);
        if (hasNoMoreVisitHistory(currentAndNextHistories, request.getSize())) {
            return MyVisitHistoriesScrollResponse.newLastScroll(currentAndNextHistories);
        }
        List<VisitHistory> currentHistories = currentAndNextHistories.subList(0, request.getSize());
        return MyVisitHistoriesScrollResponse.of(currentHistories, currentHistories.get(request.getSize() - 1).getId());
    }

    private boolean hasNoMoreVisitHistory(List<VisitHistory> visitHistories, int size) {
        return visitHistories.size() <= size;
    }

}
