package com.depromeet.threedollar.api.service.store.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveStoreDetailRequest {

    @NotNull(message = "{store.storeId.notNull}")
    private Long storeId;

    @NotNull(message = "{store.latitude.notNull}")
    private Double latitude;

    @NotNull(message = "{store.longitude.notNull}")
    private Double longitude;

    @NotNull(message = "{visit.startDate.notNull}")
    private LocalDate startDate = LocalDate.now().minusWeeks(1); //TODO: 호환성을 위해 기본값으로 차후 제거

    public static RetrieveStoreDetailRequest testInstance(Long storeId, LocalDate startDate) {
        return new RetrieveStoreDetailRequest(storeId, 34.0, 126.0, startDate);
    }

    public static RetrieveStoreDetailRequest testInstance(Long storeId) {
        return new RetrieveStoreDetailRequest(storeId, 34.0, 126.0, LocalDate.now().minusWeeks(1));
    }

}
