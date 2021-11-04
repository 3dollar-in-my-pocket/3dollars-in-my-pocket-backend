package com.depromeet.threedollar.api.service.storeimage.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddStoreImageRequest {

    @NotNull(message = "{store.storeId.notNull}")
    private Long storeId;

    public static AddStoreImageRequest testInstance(Long storeId) {
        return new AddStoreImageRequest(storeId);
    }

}
