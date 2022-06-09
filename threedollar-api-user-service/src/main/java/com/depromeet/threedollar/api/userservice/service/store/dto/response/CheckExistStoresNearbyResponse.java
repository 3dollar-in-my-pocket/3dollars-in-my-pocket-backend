package com.depromeet.threedollar.api.userservice.service.store.dto.response;

import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
