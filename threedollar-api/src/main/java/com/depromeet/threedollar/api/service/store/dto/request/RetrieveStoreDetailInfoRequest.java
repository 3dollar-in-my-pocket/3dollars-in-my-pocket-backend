package com.depromeet.threedollar.api.service.store.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveStoreDetailInfoRequest {

    @NotNull(message = "{store.storeId.notNull}")
    private Long storeId;

    @NotNull(message = "{store.latitude.notNull}")
    private Double latitude;

    @NotNull(message = "{store.longitude.notNull}")
    private Double longitude;

    @NotNull(message = "{visit.startDate.notNull}")
    private LocalDate startDate = LocalDate.now().minusMonths(1);

    @NotNull(message = "{visit.endDate.notNull}")
    private LocalDate endDate = LocalDate.now();

    public static RetrieveStoreDetailInfoRequest testInstance(Long storeId, Double latitude, Double longitude, LocalDate startDate, LocalDate endDate) {
        return new RetrieveStoreDetailInfoRequest(storeId, latitude, longitude, startDate, endDate);
    }

    public static RetrieveStoreDetailInfoRequest testInstance(Long storeId, Double latitude, Double longitude) {
        return new RetrieveStoreDetailInfoRequest(storeId, latitude, longitude, LocalDate.now().minusMonths(1), LocalDate.now());
    }

}
