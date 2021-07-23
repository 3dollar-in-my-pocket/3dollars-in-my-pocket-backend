package com.depromeet.threedollar.api.service.upload;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.depromeet.threedollar.api.service.upload.dto.request.FileUploadRequest;
import com.depromeet.threedollar.common.exception.ValidationException;
import com.depromeet.threedollar.common.utils.type.ImageType;
import com.depromeet.threedollar.external.external.s3.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class S3FileUploadServiceTest {

    private S3FileUploadService s3FileUploadService;

    @BeforeEach
    void setUp() {
        s3FileUploadService = new S3FileUploadService(new StubS3Service());
    }

    private static class StubS3Service implements S3Service {
        @Override
        public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {

        }

        @Override
        public String getFileUrl(String fileName) {
            return fileName;
        }
    }

    @Test
    void 파일이_정상적으로_업로드되면_업로드된_파일명이_반환된다() {
        // given
        MultipartFile multipartFile = new MockMultipartFile("fileName.jpeg", "fileName.jpeg", "image/jpeg", new byte[]{});

        ImageType type = ImageType.STORE;
        FileUploadRequest request = FileUploadRequest.of(type);

        // when
        String result = s3FileUploadService.uploadImage(request, multipartFile);

        // then
        assertThat(result.startsWith(type.getDirectory())).isTrue();
        assertThat(result.endsWith(".jpeg")).isTrue();
    }

    @Test
    void 파일을_업로드시_잘못된_파일명인경우_Validation_Exception이_발생한다() {
        // given
        MultipartFile multipartFile = new MockMultipartFile("fileName.jpeg", "fileName", "image/jpeg", new byte[]{});

        FileUploadRequest request = FileUploadRequest.of(ImageType.STORE);

        // when & then
        assertThatThrownBy(() -> s3FileUploadService.uploadImage(request, multipartFile)).isInstanceOf(ValidationException.class);
    }

    @Test
    void 파일을_업로드시_잘못된_ContentType인경우_Validation_Exception이_발생한다() {
        // given
        MultipartFile multipartFile = new MockMultipartFile("fileName.jpeg", "fileName.jpeg", "wrong type", new byte[]{});

        FileUploadRequest request = FileUploadRequest.of(ImageType.STORE);

        // when & then
        assertThatThrownBy(() -> s3FileUploadService.uploadImage(request, multipartFile)).isInstanceOf(ValidationException.class);
    }

}
