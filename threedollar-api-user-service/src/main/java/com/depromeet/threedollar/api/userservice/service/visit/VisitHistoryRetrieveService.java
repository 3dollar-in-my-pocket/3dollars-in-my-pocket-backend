package com.depromeet.threedollar.api.userservice.service.visit;

import com.depromeet.threedollar.api.userservice.service.visit.dto.request.RetrieveMyVisitHistoriesRequest;
import com.depromeet.threedollar.api.userservice.service.visit.dto.response.VisitHistoriesCursorResponse;
import com.depromeet.threedollar.domain.rds.core.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VisitHistoryRetrieveService {

    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional(readOnly = true)
    public VisitHistoriesCursorResponse retrieveMyVisitHistories(RetrieveMyVisitHistoriesRequest request, Long userId) {
        List<VisitHistory> visitHistoriesWithNextCursor = visitHistoryRepository.findAllByUserIdUsingCursor(userId, request.getCursor(), request.getSize() + 1);
        CursorPagingSupporter<VisitHistory> visitHistoriesCursor = CursorPagingSupporter.of(visitHistoriesWithNextCursor, request.getSize());
        return VisitHistoriesCursorResponse.of(visitHistoriesCursor);
    }

}
