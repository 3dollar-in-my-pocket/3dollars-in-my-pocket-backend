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

    @NotNull
    private Long storeId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
