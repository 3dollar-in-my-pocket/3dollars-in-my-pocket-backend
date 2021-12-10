package com.depromeet.threedollar.domain.domain.storeimage;

import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreCreator;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class StoreImageTest {

    @AutoSource
    @ParameterizedTest
    void 가게이미지의_URL을_수정한다(Long userId, String imageUrl) {
        // given
        Store store = StoreCreator.createWithDefaultMenu(userId, "가게 이름");
        StoreImage storeImage = StoreImageCreator.create(store, userId, "https://after-store-image.png");

        // when
        storeImage.updateUrl(imageUrl);

        // then
        assertThat(storeImage.getUrl()).isEqualTo(imageUrl);
    }

}
