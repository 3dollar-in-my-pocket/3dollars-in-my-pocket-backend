package com.depromeet.threedollar.api.service.visit;

import com.depromeet.threedollar.api.service.store.StoreServiceUtils;
import com.depromeet.threedollar.api.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.request.RetrieveVisitHistoryRequest;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryResponse;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VisitHistoryService {

    private final StoreRepository storeRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional
    public void addStoreVisitHistory(AddVisitHistoryRequest request, Long userId) {
        StoreServiceUtils.validateExistsStore(storeRepository, request.getStoreId());
        final LocalDate today = LocalDate.now();
        VisitHistoryServiceUtils.validateNotVisitedToday(visitHistoryRepository, request.getStoreId(), userId, today);
        visitHistoryRepository.save(request.toEntity(userId, today));
    }

    @Transactional(readOnly = true)
    public Map<LocalDate, List<VisitHistoryResponse>> retrieveStoreVisitHistory(RetrieveVisitHistoryRequest request) {
        List<VisitHistoryWithUserProjection> histories = visitHistoryRepository.findVisitWithUserByStoreIdBetweenDate(request.getStoreId(), request.getStartDate(), request.getEndDate());
        return histories.stream()
            .map(VisitHistoryResponse::of)
            .collect(Collectors.groupingBy(VisitHistoryResponse::getDateOfVisit));
    }

}
