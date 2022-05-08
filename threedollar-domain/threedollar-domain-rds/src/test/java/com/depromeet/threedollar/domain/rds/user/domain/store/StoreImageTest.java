package com.depromeet.threedollar.domain.rds.user.domain.store;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StoreImageTest {

    @Test
    void 가게이미지의_URL을_수정한다() {
        // given
        long userId = 10000L;
        String imageUrl = "https://image.png";

        Store store = StoreWithMenuCreator.builder()
            .userId(userId)
            .storeName("가게 이름")
            .build();
        StoreImage storeImage = StoreImageCreator.builder()
            .store(store)
            .userId(userId)
            .url("https://after-store-image.png")
            .build();

        // when
        storeImage.updateUrl(imageUrl);

        // then
        assertThat(storeImage.getUrl()).isEqualTo(imageUrl);
    }

}
