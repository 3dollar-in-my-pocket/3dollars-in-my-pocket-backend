package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.exception.ValidationException;
import com.depromeet.threedollar.common.utils.type.ImageType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileUtilsTest {

    @Test
    void 이미지_타입에_해당하는_디렉터리에_UUID_파일명이_생성된다() {
        // given
        String originalFileName = "image.png";
        ImageType type = ImageType.STORE;

        // when
        String result = FileUtils.createFileUuidNameWithExtension(type, originalFileName);

        // then
        assertThat(result.startsWith(type.getDirectory())).isTrue();
        assertThat(result.endsWith(".png")).isTrue();
    }


    @Test
    void 파일명을_생성할때_잘못된_파일형식일경우_VALIDATION_에러가_발생한다() {
        // given
        String originalFileName = "image";
        ImageType type = ImageType.STORE;

        // when & then
        assertThatThrownBy(() -> FileUtils.createFileUuidNameWithExtension(type, originalFileName)).isInstanceOf(ValidationException.class);
    }

    @Test
    void ContentType을_확인해서_허용된_파일_형식이_아닌경우_VALIDATION_에러가_발생한다() {
        // given
        String contentType = "video/mp4";

        // when & then
        assertThatThrownBy(() -> FileUtils.validateImageFile(contentType)).isInstanceOf(ValidationException.class);
    }

    @Test
    void ContentType을_확인해서_허용된_파일_형식일_경우_정상적으로_반환된다() {
        // given
        String contentType = "image/jpeg";

        // when & then
        FileUtils.validateImageFile(contentType);
    }

    @Test
    void ContentType을_확인해서_허용된_파일_형식일_경우_정상적으로_반환된다2() {
        // given
        String contentType = "image/png";

        // when & then
        FileUtils.validateImageFile(contentType);
    }

}
