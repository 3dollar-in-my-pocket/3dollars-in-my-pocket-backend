package com.depromeet.threedollar.api.service.visit.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveVisitHistoryRequest {

    @NotNull(message = "{store.storeId.notNull}")
    private Long storeId;

    @NotNull(message = "{visit.startDate.notNull}")
    private LocalDate startDate;

    @NotNull(message = "{visit.endDate.notNull}")
    private LocalDate endDate;

}
