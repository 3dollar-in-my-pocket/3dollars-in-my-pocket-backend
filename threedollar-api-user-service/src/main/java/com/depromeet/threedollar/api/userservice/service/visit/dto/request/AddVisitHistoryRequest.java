package com.depromeet.threedollar.api.userservice.service.visit.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistory;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddVisitHistoryRequest {

    @NotNull(message = "{store.storeId.notNull}")
    private Long storeId;

    @NotNull(message = "{visit.type.notNull}")
    private VisitType type;

    @Builder(builderMethodName = "testBuilder")
    private AddVisitHistoryRequest(Long storeId, VisitType type) {
        this.storeId = storeId;
        this.type = type;
    }

    public VisitHistory toEntity(Store store, Long userId, LocalDate dateOfVisit) {
        return VisitHistory.of(store, userId, type, dateOfVisit);
    }

}
