package com.depromeet.threedollar.external.client.s3;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface S3Client {

    void uploadFile(@NotNull MultipartFile file, @NotNull String fileName);

    String getFileUrl(@NotNull String fileName);

}
