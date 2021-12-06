package com.depromeet.threedollar.api.service.store.dto.request;

import lombok.*;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    private LocalDate startDate;

    public static RetrieveStoreDetailRequest testInstance(Long storeId, LocalDate startDate) {
        return new RetrieveStoreDetailRequest(storeId, 34.0, 126.0, startDate);
    }

    public static RetrieveStoreDetailRequest testInstance(Long storeId) {
        return new RetrieveStoreDetailRequest(storeId, 34.0, 126.0, null);
    }

    public LocalDate getStartDate() {
        // 호환성을 위해 기본적으로 일주일 내에 방문한 기록들을 조회한다.
        if (startDate == null) {
            return LocalDate.now().minusWeeks(1);
        }
        return startDate;
    }

}
