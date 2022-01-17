package com.depromeet.threedollar.application.provider.upload.dto.request;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.common.type.ImageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImageUploadRequestTest {

    @Test
    void 이미지_타입에_해당하는_디렉터리에_유니크한_파일명이_생성된다() {
        // given
        String originalFileName = "image.png";
        ImageType type = ImageType.STORE;
        ImageUploadFileRequest request = ImageUploadFileRequest.of(type);

        // when
        String result = request.createFileName(originalFileName);

        // then
        assertThat(result.startsWith(type.getDirectory())).isTrue();
        assertThat(result.endsWith(".png")).isTrue();
    }

    @ValueSource(strings = {"image", "video/mp4"})
    @ParameterizedTest
    void 허용되지_ContentType_경우_VALIDATION_FILE_TYPE_EXCEPTION(String contentType) {
        // given
        ImageUploadFileRequest request = ImageUploadFileRequest.of(ImageType.STORE);

        // when & then
        assertThatThrownBy(() -> request.validateAvailableFileType(contentType)).isInstanceOf(ValidationException.class);
    }

    @NullAndEmptySource
    @ParameterizedTest
    void ContentType이_널이거나_빈문자열일경우_VALIDATION_FILE_TYPE_EXCEPTION(String contentType) {
        // given
        ImageUploadFileRequest request = ImageUploadFileRequest.of(ImageType.STORE);

        // when & then
        assertThatThrownBy(() -> request.validateAvailableFileType(contentType)).isInstanceOf(ValidationException.class);
    }

    @ValueSource(strings = {"image/jpeg", "image/png"})
    @ParameterizedTest
    void 허용된_ContentType_경우_정상적으로_반환된다(String contentType) {
        // given
        ImageUploadFileRequest request = ImageUploadFileRequest.of(ImageType.STORE);

        // when & then
        request.validateAvailableFileType(contentType);
    }

}
