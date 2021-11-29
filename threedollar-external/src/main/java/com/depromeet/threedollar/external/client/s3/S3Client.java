package com.depromeet.threedollar.external.client.s3;

import org.springframework.web.multipart.MultipartFile;

public interface S3Client {

    void uploadFile(MultipartFile file, String fileName);

    String getFileUrl(String fileName);

}
