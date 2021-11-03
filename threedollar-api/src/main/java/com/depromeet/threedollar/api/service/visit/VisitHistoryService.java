package com.depromeet.threedollar.api.service.visit;

import com.depromeet.threedollar.api.service.store.StoreServiceUtils;
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.request.RetrieveMyVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.request.RetrieveVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.response.MyVisitHistoriesScrollResponse;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryWithUserResponse;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<VisitHistoryWithUserResponse> retrieveVisitHistories(RetrieveVisitHistoryRequest request) {
        StoreServiceUtils.validateExistsStore(storeRepository, request.getStoreId());
        List<VisitHistoryWithUserProjection> histories = visitHistoryRepository.findAllVisitWithUserByStoreIdBetweenDate(request.getStoreId(), request.getStartDate(), request.getEndDate());
        return histories.stream()
            .map(VisitHistoryWithUserResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MyVisitHistoriesScrollResponse retrieveMyVisitHistories(RetrieveMyVisitHistoryRequest request, Long userId) {
        List<VisitHistory> currentAndNextHistories = visitHistoryRepository.findAllByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1);
        if (currentAndNextHistories.size() <= request.getSize()) {
            return MyVisitHistoriesScrollResponse.newLastScroll(currentAndNextHistories);
        }

        List<VisitHistory> currentHistories = currentAndNextHistories.subList(0, request.getSize());
        return MyVisitHistoriesScrollResponse.of(currentHistories, currentHistories.get(request.getSize() - 1).getId());
    }

}
