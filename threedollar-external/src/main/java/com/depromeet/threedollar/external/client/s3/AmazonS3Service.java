package com.depromeet.threedollar.external.client.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.depromeet.threedollar.external.client.s3.dto.properties.AmazonCloudFrontProperties;
import com.depromeet.threedollar.external.client.s3.dto.properties.AmazonS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class AmazonS3Service implements S3Service {

    private final AmazonS3 amazonS3;

    private final AmazonS3Properties s3Properties;
    private final AmazonCloudFrontProperties cloudFrontProperties;

    @Override
    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        amazonS3.putObject(new PutObjectRequest(s3Properties.getBucket(), fileName, inputStream, objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public String getFileUrl(String fileName) {
        return cloudFrontProperties.getFullPathFileUrl(fileName);
    }

}
