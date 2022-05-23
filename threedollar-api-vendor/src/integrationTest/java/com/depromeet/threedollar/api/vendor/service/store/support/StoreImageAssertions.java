package com.depromeet.threedollar.api.vendor.service.store.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.depromeet.threedollar.api.vendor.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.domain.rds.vendor.domain.TestHelper;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreImage;
import com.depromeet.threedollar.domain.rds.vendor.domain.store.StoreImageStatus;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@TestHelper
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StoreImageAssertions {

    public static void assertStoreImage(StoreImage storeImage, Long storeId, Long userId, String imageUrl, StoreImageStatus status) {
        assertAll(
            () -> assertThat(storeImage.getStoreId()).isEqualTo(storeId),
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
