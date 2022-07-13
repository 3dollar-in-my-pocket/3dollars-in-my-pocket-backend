package com.depromeet.threedollar.api.userservice.service.store;

import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImage;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.E404_NOT_EXISTS_STORE_IMAGE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class StoreImageServiceHelper {

    @NotNull
    static StoreImage findStoreImageById(StoreImageRepository storeImageRepository, Long storeImageId) {
        StoreImage storeImage = storeImageRepository.findStoreImageById(storeImageId);
        if (storeImage == null) {
            throw new NotFoundException(String.format("해당하는 가게 이미지 (%s)는 존재하지 않습니다", storeImageId), E404_NOT_EXISTS_STORE_IMAGE);
        }
        return storeImage;
    }

}
