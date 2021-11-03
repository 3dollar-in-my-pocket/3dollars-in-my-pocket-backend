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

    public static RetrieveMyVisitHistoryRequest testInstance(int size, Long cursor) {
        return new RetrieveMyVisitHistoryRequest(size, cursor);
    }

}