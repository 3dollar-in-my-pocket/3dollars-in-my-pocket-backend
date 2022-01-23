package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.common.type.FileType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileContentTypeUtilsTest {

    @ValueSource(strings = {"image", "video/mp4"})
    @ParameterizedTest
    void 허용되지_ContentType_경우_VALIDATION_FILE_TYPE_EXCEPTION(String contentType) {
        // given
        FileType fileType = FileType.STORE_IMAGE;

        // when & then
        assertThatThrownBy(() -> FileContentTypeUtils.validateAvailableContentType(contentType, fileType)).isInstanceOf(ValidationException.class);
    }

    @NullAndEmptySource
    @ParameterizedTest
    void ContentType이_널이거나_빈문자열일경우_VALIDATION_FILE_TYPE_EXCEPTION(String contentType) {
        // given
        FileType fileType = FileType.STORE_IMAGE;

        // when & then
        assertThatThrownBy(() -> FileContentTypeUtils.validateAvailableContentType(contentType, fileType)).isInstanceOf(ValidationException.class);
    }

    @ValueSource(strings = {"image/jpeg", "image/png"})
    @ParameterizedTest
    void 허용된_ContentType_경우_정상적으로_반환된다(String contentType) {
        // given
        FileType fileType = FileType.STORE_IMAGE;

        // when & then
        FileContentTypeUtils.validateAvailableContentType(contentType, fileType);
    }

}
