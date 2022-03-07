package com.depromeet.threedollar.api.user.service.visit.dto.request;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveMyVisitHistoriesRequest {

    @Min(value = 1, message = "{common.size.min}")
    @Max(value = 100, message = "{common.size.max}")
    private int size;

    @Nullable
    private Long cursor;

    public static RetrieveMyVisitHistoriesRequest testInstance(int size, Long cursor) {
        return new RetrieveMyVisitHistoriesRequest(size, cursor);
    }

}
