package com.depromeet.threedollar.api.provider.upload;

import com.depromeet.threedollar.api.provider.upload.dto.request.ImageUploadFileRequest;
import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.domain.domain.common.ImageType;
import com.depromeet.threedollar.external.client.s3.S3Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
class S3UploadProviderTest {

    @Autowired
    private S3UploadProvider s3UploadProvider;

    @MockBean
    private S3Client s3Provider;

    @Test
    void 이미지_업로드시_S3로_업로드되고_해당_URL을_가져온다() {
        // given
        String fileName = "fileName.jpeg";
        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "image/jpeg", new byte[]{});

        // when
        s3UploadProvider.uploadFile(ImageUploadFileRequest.of(ImageType.STORE), multipartFile);

        // then
        verify(s3Provider, times(1)).uploadFile(any(), any(), any(String.class));
        verify(s3Provider, times(1)).getFileUrl(any(String.class));
    }

    @Test
    void 파일_업로드중_IO_Exception_이_발생하면_Validation_Exception() throws IOException {
        // given
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getInputStream()).thenThrow(IOException.class);

        // when & then
        assertThatThrownBy(() -> s3UploadProvider.uploadFile(ImageUploadFileRequest.of(ImageType.STORE), multipartFile)).isInstanceOf(ValidationException.class);
    }

}
