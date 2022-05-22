package com.depromeet.threedollar.api.user.service.store.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveMyStoresRequest {

    @Min(value = 1, message = "{common.size.min}")
    @Max(value = 100, message = "{common.size.max}")
    private int size;

    @Nullable
    private Long cursor;

    @Builder(builderMethodName = "testBuilder")
    private RetrieveMyStoresRequest(int size, @Nullable Long cursor) {
        this.size = size;
        this.cursor = cursor;
    }

}
