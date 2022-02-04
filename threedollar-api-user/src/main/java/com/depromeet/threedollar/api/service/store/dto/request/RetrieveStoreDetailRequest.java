package com.depromeet.threedollar.api.service.store.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveStoreDetailRequest {

    @NotNull(message = "{store.storeId.notNull}")
    private Long storeId;

    @PastOrPresent(message = "{visit.startDate.pastOrPresent}")
    @NotNull(message = "{visit.startDate.notNull}")
    private LocalDate startDate;

    public static RetrieveStoreDetailRequest testInstance(Long storeId, LocalDate startDate) {
        return new RetrieveStoreDetailRequest(storeId, startDate);
    }

    public static RetrieveStoreDetailRequest testInstance(Long storeId) {
        return new RetrieveStoreDetailRequest(storeId, null);
    }

}
