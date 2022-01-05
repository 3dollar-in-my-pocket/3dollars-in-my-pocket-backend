package com.depromeet.threedollar.common.type;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImageTypeTest {

    @Test
    void 이미지_종류와_버전을_명시한_파일_경로를_반환한다() {
        // given
        String fileName = "image.png";
        ImageType type = ImageType.STORE;

        // when
        String fullDirectory = type.getFileNameWithDirectory(fileName);

        // then
        assertThat(fullDirectory).isEqualTo("store/v2/image.png");
    }

}
