package com.depromeet.threedollar.api.service.visit;

import com.depromeet.threedollar.domain.event.visit.VisitHistoryAddedEvent;
import com.depromeet.threedollar.api.service.store.StoreServiceUtils;
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.request.RetrieveMyVisitHistoriesRequest;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoriesScrollResponse;
import com.depromeet.threedollar.domain.collection.common.ScrollPaginationCollection;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VisitHistoryService {

    private final ApplicationEventPublisher eventPublisher;
    private final StoreRepository storeRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional
    public void addVisitHistory(AddVisitHistoryRequest request, Long userId, LocalDate dateOfVisit) {
        Store store = StoreServiceUtils.findStoreById(storeRepository, request.getStoreId());
        VisitHistoryServiceUtils.validateNotVisitedToday(visitHistoryRepository, request.getStoreId(), userId, dateOfVisit);
        VisitHistory visitHistory = visitHistoryRepository.save(request.toEntity(store, userId, dateOfVisit));
        eventPublisher.publishEvent(VisitHistoryAddedEvent.of(visitHistory.getId(), userId));
    }

    @Transactional(readOnly = true)
    public VisitHistoriesScrollResponse retrieveMyVisitHistories(RetrieveMyVisitHistoriesRequest request, Long userId) {
        List<VisitHistory> visitHistoriesWithNextCursor = visitHistoryRepository.findAllByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1);
        ScrollPaginationCollection<VisitHistory> visitHistoriesScroll = ScrollPaginationCollection.of(visitHistoriesWithNextCursor, request.getSize());
        return VisitHistoriesScrollResponse.of(visitHistoriesScroll);
    }

}
