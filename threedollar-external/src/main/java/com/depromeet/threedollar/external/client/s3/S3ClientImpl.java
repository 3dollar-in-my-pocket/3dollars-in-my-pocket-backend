package com.depromeet.threedollar.external.client.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.external.client.s3.dto.properties.AmazonCloudFrontProperties;
import com.depromeet.threedollar.external.client.s3.dto.properties.AmazonS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.depromeet.threedollar.common.exception.ErrorCode.VALIDATION_FILE_UPLOAD_EXCEPTION;

@RequiredArgsConstructor
@Component
public class S3ClientImpl implements S3Client {

    private final AmazonS3 amazonS3;

    private final AmazonS3Properties s3Properties;
    private final AmazonCloudFrontProperties cloudFrontProperties;

    @Override
    public void uploadFile(MultipartFile file, String fileName) {
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(s3Properties.getBucket(), fileName, inputStream, createObjectMetadata(file))
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ValidationException(String.format("파일 (%s) 입력 스트림을 가져오는 중 에러가 발생하였습니다", file.getOriginalFilename()), VALIDATION_FILE_UPLOAD_EXCEPTION);
        }
    }

    private ObjectMetadata createObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    @Override
    public String getFileUrl(String fileName) {
        return cloudFrontProperties.getFullPathFileUrl(fileName);
    }

}
