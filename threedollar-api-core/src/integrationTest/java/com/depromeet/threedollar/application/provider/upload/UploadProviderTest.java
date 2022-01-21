package com.depromeet.threedollar.application.provider.upload;

import com.depromeet.threedollar.application.provider.upload.dto.request.ImageUploadFileRequest;
import com.depromeet.threedollar.common.type.ImageType;
import com.depromeet.threedollar.external.client.s3.S3Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;

@SpringBootTest
class UploadProviderTest {

    @Autowired
    private UploadProvider uploadProvider;

    @MockBean
    private S3Client s3Provider;

    @Test
    void 이미지_업로드시_S3로_업로드되고_해당_URL을_가져온다() {
        // given
        String fileName = "fileName.jpeg";
        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "image/jpeg", new byte[]{});

        // when
        uploadProvider.uploadFile(ImageUploadFileRequest.of(ImageType.STORE), multipartFile);

        // then
        verify(s3Provider, times(1)).uploadFile(any(), any(String.class));
        verify(s3Provider, times(1)).getFileUrl(any(String.class));
    }

}
