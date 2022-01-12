package com.depromeet.threedollar.domain.user.domain.storeimage;

import com.depromeet.threedollar.domain.user.domain.store.Store;
import com.depromeet.threedollar.domain.user.domain.store.StoreCreator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StoreImageTest {

    @Test
    void 가게이미지의_URL을_수정한다() {
        // given
        Long userId = 10000L;
        String imageUrl = "https://image.png";

        Store store = StoreCreator.createWithDefaultMenu(userId, "가게 이름");
        StoreImage storeImage = StoreImageCreator.create(store, userId, "https://after-store-image.png");

        // when
        storeImage.updateUrl(imageUrl);

        // then
        assertThat(storeImage.getUrl()).isEqualTo(imageUrl);
    }

}
