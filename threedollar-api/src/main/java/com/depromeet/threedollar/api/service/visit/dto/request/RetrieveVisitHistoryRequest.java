package com.depromeet.threedollar.api.service.visit.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ToString
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

    public static RetrieveVisitHistoryRequest testInstance(Long storeId, LocalDate startDate, LocalDate endDate) {
        return new RetrieveVisitHistoryRequest(storeId, startDate, endDate);
    }

}
