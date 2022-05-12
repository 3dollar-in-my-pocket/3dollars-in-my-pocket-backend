package com.depromeet.threedollar.domain.rds.user.domain.store;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StoreImageTest {

    @Test
    void 가게이미지의_URL을_수정한다() {
        // given
        long userId = 10000L;
        long storeId = 99999L;
        String imageUrl = "https://image.png";

        StoreImage storeImage = StoreImageCreator.create(storeId, userId, "https://after-store-image.png");

        // when
        storeImage.updateUrl(imageUrl);

        // then
        assertThat(storeImage.getUrl()).isEqualTo(imageUrl);
    }

}
