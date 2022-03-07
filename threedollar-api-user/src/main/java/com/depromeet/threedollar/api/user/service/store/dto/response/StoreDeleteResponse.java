package com.depromeet.threedollar.api.user.service.store.dto.response;

import lombok.*;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreDeleteResponse {

    private Boolean isDeleted;

    public static StoreDeleteResponse of(boolean isDeleted) {
        return new StoreDeleteResponse(isDeleted);
    }

}
