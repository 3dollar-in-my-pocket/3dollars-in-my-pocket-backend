package com.depromeet.threedollar.api.service.storeimage;

import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImageRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.depromeet.threedollar.common.exception.ErrorCode.NOT_FOUND_STORE_IMAGE_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class StoreImageServiceUtils {

    static StoreImage findStoreImageById(StoreImageRepository storeImageRepository, Long storeImageId) {
        StoreImage storeImage = storeImageRepository.findStoreImageById(storeImageId);
        if (storeImage == null) {
            throw new NotFoundException(String.format("해당하는 가게 이미지 (%s)는 존재하지 않습니다", storeImageId), NOT_FOUND_STORE_IMAGE_EXCEPTION);
        }
        return storeImage;
    }

}
