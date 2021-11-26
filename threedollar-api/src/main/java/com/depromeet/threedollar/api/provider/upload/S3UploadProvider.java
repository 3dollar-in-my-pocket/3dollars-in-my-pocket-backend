package com.depromeet.threedollar.api.provider.upload;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.depromeet.threedollar.api.provider.upload.dto.request.UploadFileRequest;
import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.external.client.s3.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.depromeet.threedollar.common.exception.ErrorCode.VALIDATION_FILE_UPLOAD_EXCEPTION;

@RequiredArgsConstructor
@Component
public class S3UploadProvider implements UploadProvider {

    private final S3Client s3Client;

    @Override
    public String uploadFile(UploadFileRequest request, MultipartFile file) {
        request.validateAvailableFileType(file.getContentType());
        String fileName = request.createFileName(file.getOriginalFilename());

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.uploadFile(inputStream, createObjectMetadata(file), fileName);
        } catch (IOException e) {
            throw new ValidationException(String.format("파일 (%s) 입력 스트림을 가져오는 중 에러가 발생하였습니다", file.getOriginalFilename()), VALIDATION_FILE_UPLOAD_EXCEPTION);
        }
        return s3Client.getFileUrl(fileName);
    }

    private ObjectMetadata createObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

}