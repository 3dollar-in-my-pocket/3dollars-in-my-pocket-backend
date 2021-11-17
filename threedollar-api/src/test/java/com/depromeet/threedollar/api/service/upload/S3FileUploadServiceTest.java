package com.depromeet.threedollar.api.service.upload;

import com.depromeet.threedollar.api.service.upload.dto.request.ImageUploadRequest;
import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.domain.domain.common.ImageType;
import com.depromeet.threedollar.external.client.s3.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class S3FileUploadServiceTest {

    @Autowired
    private S3UploadService s3UploadService;

    @MockBean
    private S3Service s3Service;

    @Test
    void 이미지_업로드시_S3로_업로드되고_해당_URL을_가져온다() {
        // given
        String fileName = "fileName.jpeg";
        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "image/jpeg", new byte[]{});

        // when
        s3UploadService.uploadFile(ImageUploadRequest.of(ImageType.STORE), multipartFile);

        // then
        verify(s3Service, times(1)).uploadFile(any(), any(), any(String.class));
        verify(s3Service, times(1)).getFileUrl(any(String.class));
    }

    @Test
    void 파일_업로드중_IO_Exception_이_발생하면_Validation_Exception() throws IOException {
        // given
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenThrow(IOException.class);

        // when & then
        assertThatThrownBy(() -> s3UploadService.uploadFile(ImageUploadRequest.of(ImageType.STORE), multipartFile)).isInstanceOf(ValidationException.class);
    }

}
