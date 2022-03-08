package com.depromeet.threedollar.api.user.service.visit.dto.request;

import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitType;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddVisitHistoryRequest {

    @NotNull(message = "{store.storeId.notNull}")
    private Long storeId;

    @NotNull(message = "{visit.type.notNull}")
    private VisitType type;

    public static AddVisitHistoryRequest testInstance(Long storeId, VisitType type) {
        return new AddVisitHistoryRequest(storeId, type);
    }

    public VisitHistory toEntity(Store store, Long userId, LocalDate dateOfVisit) {
        return VisitHistory.of(store, userId, type, dateOfVisit);
    }

}