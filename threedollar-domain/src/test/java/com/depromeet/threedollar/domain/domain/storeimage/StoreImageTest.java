package com.depromeet.threedollar.domain.domain.storeimage;

import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class StoreImageTest {

    @AutoSource
    @ParameterizedTest
    void 가게이미지의_URL을_수정한다(Long storeId, Long userId, String imageUrl) {
        // given
        StoreImage storeImage = StoreImageCreator.create(storeId, userId, "https://after-store-image.png");

        // when
        storeImage.updateUrl(imageUrl);

        // then
        assertThat(storeImage.getUrl()).isEqualTo(imageUrl);
    }

}
