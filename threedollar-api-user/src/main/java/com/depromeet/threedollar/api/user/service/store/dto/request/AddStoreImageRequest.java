package com.depromeet.threedollar.api.user.service.store.dto.request;

import javax.validation.constraints.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddStoreImageRequest {

    @NotNull(message = "{store.storeId.notNull}")
    private Long storeId;

    @Builder(builderMethodName = "testBuilder")
    private AddStoreImageRequest(Long storeId) {
        this.storeId = storeId;
    }

    public StoreImage toEntity(Long userId, String imageUrl) {
        return StoreImage.newInstance(storeId, userId, imageUrl);
    }

}
