package com.depromeet.threedollar.api.service.visit.dto.request;

import lombok.*;

import javax.validation.constraints.Min;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveMyVisitHistoryRequest {

    @Min(value = 1, message = "{common.size.min}")
    private int size;

    private Long cursor;

    private Long cachingTotalElements;

    public static RetrieveMyVisitHistoryRequest testInstance(int size, Long cursor, Long cachingTotalElements) {
        return new RetrieveMyVisitHistoryRequest(size, cursor, cachingTotalElements);
    }

}
