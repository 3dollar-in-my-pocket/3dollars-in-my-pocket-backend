package com.depromeet.threedollar.api.userservice.service.visit;

import com.depromeet.threedollar.api.core.service.service.userservice.store.StoreServiceHelper;
import com.depromeet.threedollar.api.userservice.service.visit.dto.request.AddVisitHistoryRequest;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.E409_DUPLICATE_VISIT_HISTORY;

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
            throw new ConflictException(String.format("유저(%s)는 해당 날짜(%s)에 유저 가게(%s)를 이미 방문 인증하였습니다", userId, date, storeId), E409_DUPLICATE_VISIT_HISTORY);
        }
    }

}
