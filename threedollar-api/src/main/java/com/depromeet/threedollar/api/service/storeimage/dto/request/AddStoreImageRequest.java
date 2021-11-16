package com.depromeet.threedollar.api.service.storeimage.dto.request;

import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;
import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddStoreImageRequest {

    @NotNull(message = "{store.storeId.notNull}")
    private Long storeId;

    public StoreImage toEntity(Long userId, String imageUrl) {
        return StoreImage.newInstance(storeId, userId, imageUrl);
    }

    public static AddStoreImageRequest testInstance(Long storeId) {
        return new AddStoreImageRequest(storeId);
    }

}
