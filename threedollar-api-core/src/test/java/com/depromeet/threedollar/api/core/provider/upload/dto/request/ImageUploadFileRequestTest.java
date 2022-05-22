package com.depromeet.threedollar.api.core.provider.upload.dto.request;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import com.depromeet.threedollar.common.exception.model.ForbiddenException;
import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.common.type.FileType;

class ImageUploadFileRequestTest {

    @Test
    void 해당_이미지_업로드를_허용하지_않는_서버인경우_403에러가_발생한다() {
        // given
        ApplicationType applicationType = ApplicationType.USER_API;

        ImageUploadFileRequest request = ImageUploadFileRequest.of(new MockMultipartFile("name", "originFileName", "image/jpeg", new byte[]{}), FileType.MEDAL_IMAGE, applicationType);

        // when & then
        assertThatThrownBy(request::validateAvailableUploadFile).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 해당_이미지_업로드를_허용하는경우_에러가_발생하지_않는다() {
        // given
        ApplicationType applicationType = ApplicationType.ADMIN_API;
        FileType fileType = FileType.MEDAL_IMAGE;
        ImageUploadFileRequest request = ImageUploadFileRequest.of(new MockMultipartFile("name", "originFileName", "image/jpeg", new byte[]{}), fileType, applicationType);

        // when & then
        assertDoesNotThrow(request::validateAvailableUploadFile);
    }

}
