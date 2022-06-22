package com.depromeet.threedollar.api.userservice.service.store;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.NOT_FOUND_STORE_IMAGE;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImage;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageRepository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class StoreImageServiceHelper {

    @NotNull
    static StoreImage findStoreImageById(StoreImageRepository storeImageRepository, Long storeImageId) {
        StoreImage storeImage = storeImageRepository.findStoreImageById(storeImageId);
        if (storeImage == null) {
            throw new NotFoundException(String.format("해당하는 가게 이미지 (%s)는 존재하지 않습니다", storeImageId), NOT_FOUND_STORE_IMAGE);
        }
        return storeImage;
    }

}
