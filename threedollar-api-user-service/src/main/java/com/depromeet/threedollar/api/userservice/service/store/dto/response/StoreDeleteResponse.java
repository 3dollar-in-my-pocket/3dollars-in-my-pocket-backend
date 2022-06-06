package com.depromeet.threedollar.api.userservice.service.store.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
