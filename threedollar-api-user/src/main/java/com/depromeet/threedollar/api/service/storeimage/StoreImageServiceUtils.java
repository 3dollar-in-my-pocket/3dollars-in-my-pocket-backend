package com.depromeet.threedollar.api.service.storeimage;

import com.depromeet.threedollar.common.exception.model.NotFoundException;
import com.depromeet.threedollar.domain.user.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.user.domain.storeimage.StoreImageRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.NOTFOUND_STORE_IMAGE;

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
