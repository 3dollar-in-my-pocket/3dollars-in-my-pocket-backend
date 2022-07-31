package com.depromeet.threedollar.api.userservice.service.visit;

import com.depromeet.threedollar.api.userservice.service.visit.dto.request.RetrieveMyVisitHistoriesRequest;
import com.depromeet.threedollar.api.userservice.service.visit.dto.response.VisitHistoriesCursorResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.collection.VisitHistoryCursorPaging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VisitHistoryRetrieveService {

    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional(readOnly = true)
    public VisitHistoriesCursorResponse retrieveMyVisitHistories(RetrieveMyVisitHistoriesRequest request, Long userId) {
        VisitHistoryCursorPaging visitHistoriesCursor = visitHistoryRepository.findAllByUserIdUsingCursor(userId, request.getCursor(), request.getSize());
        return VisitHistoriesCursorResponse.of(visitHistoriesCursor);
    }

}
