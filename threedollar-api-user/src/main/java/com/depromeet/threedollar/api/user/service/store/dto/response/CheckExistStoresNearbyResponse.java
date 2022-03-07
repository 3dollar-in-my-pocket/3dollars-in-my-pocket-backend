package com.depromeet.threedollar.api.user.service.store.dto.response;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckExistStoresNearbyResponse {

    @NotNull
    private Boolean isExists;

    public static CheckExistStoresNearbyResponse of(boolean isExists) {
        return new CheckExistStoresNearbyResponse(isExists);
    }

}
