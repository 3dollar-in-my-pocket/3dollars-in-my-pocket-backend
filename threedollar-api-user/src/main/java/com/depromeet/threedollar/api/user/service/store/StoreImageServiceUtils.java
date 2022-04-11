package com.depromeet.threedollar.api.user.service.store;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.NOTFOUND_STORE_IMAGE;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImageRepository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class StoreImageServiceUtils {

    @NotNull
    static StoreImage findStoreImageById(StoreImageRepository storeImageRepository, Long storeImageId) {
        StoreImage storeImage = storeImageRepository.findStoreImageById(storeImageId);
        if (storeImage == null) {
            throw new NotFoundException(String.format("해당하는 가게 이미지 (%s)는 존재하지 않습니다", storeImageId), NOTFOUND_STORE_IMAGE);
        }
        return storeImage;
    }

}
