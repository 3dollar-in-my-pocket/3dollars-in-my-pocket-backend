package com.depromeet.threedollar.api.userservice.service.store.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveStoreDetailRequest {

    @NotNull(message = "{store.storeId.notNull}")
    private Long storeId;

    @PastOrPresent(message = "{visit.startDate.pastOrPresent}")
    @NotNull(message = "{visit.startDate.notNull}")
    private LocalDate startDate;

    @Builder(builderMethodName = "testBuilder")
    private RetrieveStoreDetailRequest(Long storeId, LocalDate startDate) {
        this.storeId = storeId;
        this.startDate = startDate;
    }

}
