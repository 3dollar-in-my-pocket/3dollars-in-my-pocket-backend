package com.depromeet.threedollar.external.client.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.external.client.storage.property.AmazonCloudFrontProperty;
import com.depromeet.threedollar.external.client.storage.property.AmazonS3Property;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class S3FileStorageClient implements FileStorageClient {

    private final AmazonS3 amazonS3;

    private final AmazonS3Property s3Property;
    private final AmazonCloudFrontProperty cloudFrontProperty;

    @Override
    public void uploadFile(@NotNull MultipartFile file, @NotNull String fileName) {
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(s3Property.getBucket(), fileName, inputStream, createObjectMetadata(file))
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new InternalServerException(String.format("파일 (%s) 입력 스트림을 가져오는 중 에러가 발생하였습니다", file.getOriginalFilename()));
        }
    }

    private ObjectMetadata createObjectMetadata(@NotNull MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    @Override
    public String getFileUrl(@NotNull String fileName) {
        return cloudFrontProperty.getFullPathFileUrl(fileName);
    }

}
