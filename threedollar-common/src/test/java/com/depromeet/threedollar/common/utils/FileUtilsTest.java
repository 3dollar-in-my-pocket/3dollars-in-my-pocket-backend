package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.exception.validation.ValidationFileTypeException;
import com.depromeet.threedollar.common.type.ImageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(strings = {"image", "video/mp4"})
    void 허용되지_ContentType_경우_VALIDATION_FILE_TYPE_EXCEPTION(String contentType) {
        // when & then
        assertThatThrownBy(() -> FileUtils.createFileUuidNameWithExtension(ImageType.STORE, contentType)).isInstanceOf(ValidationFileTypeException.class);
    }

    @Test
    void ContentType이_널인경우_VALIDATION_FILE_TYPE_EXCEPTION() {
        // when & then
        assertThatThrownBy(() -> FileUtils.validateAvailableImageFile(null)).isInstanceOf(ValidationFileTypeException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"image/jpeg", "image/png"})
    void 허용된_ContentType_경우_정상적으로_반환된다(String contentType) {
        // when & then
        FileUtils.validateAvailableImageFile(contentType);
    }

}
