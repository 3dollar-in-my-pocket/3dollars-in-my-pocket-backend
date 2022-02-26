package com.depromeet.threedollar.application.provider.upload.dto.request;

import com.depromeet.threedollar.common.exception.model.ForbiddenException;
import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.common.type.FileType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImageUploadFileRequestTest {

    @Test
    void 해당_이미지_업로드를_허용하지_않는_서버인경우_403에러가_발생한다() {
        // given
        ApplicationType applicationType = ApplicationType.USER_API;

        // when & then
        assertThatThrownBy(() -> ImageUploadFileRequest.of(FileType.MEDAL_IMAGE, applicationType)).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 해당_이미지_업로드를_허용하는경우_성공한다() {
        // given
        ApplicationType applicationType = ApplicationType.ADMIN_API;
        FileType fileType = FileType.MEDAL_IMAGE;

        // when
        ImageUploadFileRequest request = ImageUploadFileRequest.of(fileType, applicationType);

        // then
        assertThat(request.getType()).isEqualTo(fileType);
    }

}
