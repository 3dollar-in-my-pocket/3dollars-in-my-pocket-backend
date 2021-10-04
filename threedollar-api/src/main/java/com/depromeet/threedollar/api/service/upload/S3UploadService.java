package com.depromeet.threedollar.api.service.upload;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.depromeet.threedollar.api.service.upload.dto.request.UploadRequest;
import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.external.client.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class S3UploadService implements UploadService {

    private final S3Service s3Service;

    @Override
    public String uploadFile(UploadRequest request, MultipartFile file) {
        request.validate(file.getContentType());
        final String fileName = request.createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new InternalServerException(String.format("파일 (%s) 입력 스트림을 가져오는 중 에러가 발생하였습니다", file.getOriginalFilename()));
        }
        return s3Service.getFileUrl(fileName);
    }

}
