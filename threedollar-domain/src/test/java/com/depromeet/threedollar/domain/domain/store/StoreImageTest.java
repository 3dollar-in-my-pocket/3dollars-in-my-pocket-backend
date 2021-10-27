package com.depromeet.threedollar.domain.domain.store;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StoreImageTest {

    @Test
    void 가게이미지의_URL을_수정한다() {
        // given
        String url = "https://after-store-image.png";

        Long storeId = 100000L;
        Long userId = 200000L;
        StoreImage storeImage = StoreImageCreator.create(storeId, userId, "https://store-image.png");

        // when
        storeImage.updateUrl(url);

        // then
        assertThat(storeImage.getUrl()).isEqualTo(url);
    }

}
