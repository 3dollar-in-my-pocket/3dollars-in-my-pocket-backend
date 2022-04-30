package com.depromeet.threedollar.common.type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.depromeet.threedollar.common.exception.model.ForbiddenException;
import com.depromeet.threedollar.common.exception.model.InvalidException;

class FileTypeTest {

    @Nested
    class ValidateAvailableUploadInModuleTest {

        @Test
        void 해당_파일을_업로드할_수_있는_모듈인경우_에러가_발생하지_않는다() {
            // given
            FileType fileType = FileType.BOSS_STORE_CERTIFICATION_IMAGE;

            // when & then
            assertDoesNotThrow(() -> fileType.validateAvailableUploadInModule(ApplicationType.BOSS_API));
        }

        @Test
        void 해당_파일을_사용할수_있는_모듈인지_확인한다_아닌경우_ForbidenException() {
            // given
            FileType fileType = FileType.BOSS_STORE_CERTIFICATION_IMAGE;

            // when & then
            assertThatThrownBy(() -> fileType.validateAvailableUploadInModule(ApplicationType.USER_API)).isInstanceOf(ForbiddenException.class);
        }

        @ValueSource(strings = {"image", "video/mp4"})
        @ParameterizedTest
        void 허용되지_ContentType_경우_VALIDATION_FILE_TYPE_EXCEPTION(String contentType) {
            // given
            FileType imageFileType = FileType.STORE_IMAGE;

            // when & then
            assertThatThrownBy(() -> imageFileType.validateAvailableContentType(contentType)).isInstanceOf(InvalidException.class);
        }

        @NullAndEmptySource
        @ParameterizedTest
        void ContentType이_널이거나_빈문자열일경우_VALIDATION_FILE_TYPE_EXCEPTION(String contentType) {
            // given
            FileType imageFileType = FileType.STORE_IMAGE;

            // when & then
            assertThatThrownBy(() -> imageFileType.validateAvailableContentType(contentType)).isInstanceOf(InvalidException.class);
        }

        @ValueSource(strings = {"image/jpeg", "image/png"})
        @ParameterizedTest
        void 허용된_ContentType_경우_정상적으로_반환된다(String contentType) {
            // given
            FileType imageFileType = FileType.STORE_IMAGE;

            // when & then
            imageFileType.validateAvailableContentType(contentType);
        }

    }

    @Nested
    class CreateUniqueFileNameWithExtensionTest {

        @Test
        void 이미지_타입에_해당하는_디렉터리에_유니크한_파일명이_생성된다() {
            // given
            String originalFileName = "image.png";
            FileType type = FileType.STORE_IMAGE;

            // when
            String result = type.createUniqueFileNameWithExtension(originalFileName);

            // then
            assertThat(result).startsWith(type.getDirectory())
                .endsWith(".png");
        }

        @Test
        void originalFileName이_없는경우_InvalidException() {
            // when & then
            assertThatThrownBy(() -> FileType.ADVERTISEMENT_IMAGE.createUniqueFileNameWithExtension(null)).isInstanceOf(InvalidException.class);
        }

    }

}
