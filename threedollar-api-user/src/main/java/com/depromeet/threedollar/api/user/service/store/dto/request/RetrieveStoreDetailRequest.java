package com.depromeet.threedollar.api.user.service.store.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

}
