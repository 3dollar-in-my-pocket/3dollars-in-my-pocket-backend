package com.depromeet.threedollar.api.user.controller.store.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.depromeet.threedollar.api.user.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.domain.rds.user.domain.TestHelper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@TestHelper
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StoreImageAssertions {

    public static void assertStoreImageResponse(StoreImageResponse storeImageResponse, Long imageId, String imageUrl) {
        assertAll(
            () -> assertThat(storeImageResponse.getImageId()).isEqualTo(imageId),
            () -> assertThat(storeImageResponse.getUrl()).isEqualTo(imageUrl)
        );
    }

}