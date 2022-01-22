package com.depromeet.threedollar.testhelper.assertion;

import com.depromeet.threedollar.api.service.storeimage.dto.response.StoreImageResponse;
import com.depromeet.threedollar.domain.user.domain.TestHelper;
import com.depromeet.threedollar.domain.user.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.user.domain.storeimage.StoreImageStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestHelper
public final class StoreImageAssertionHelper {

    public static void assertStoreImage(StoreImage storeImage, Long storeId, Long userId, String imageUrl, StoreImageStatus status) {
        assertAll(
            () -> assertThat(storeImage.getStore().getId()).isEqualTo(storeId),
            () -> assertThat(storeImage.getUserId()).isEqualTo(userId),
            () -> assertThat(storeImage.getUrl()).isEqualTo(imageUrl),
            () -> assertThat(storeImage.getStatus()).isEqualTo(status)
        );
    }

    public static void assertStoreImageResponse(StoreImageResponse storeImageResponse, Long imageId, String imageUrl) {
        assertAll(
            () -> assertThat(storeImageResponse.getImageId()).isEqualTo(imageId),
            () -> assertThat(storeImageResponse.getUrl()).isEqualTo(imageUrl)
        );
    }

}
