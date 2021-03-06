package com.depromeet.threedollar.infrastructure.s3.infra;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageClient {

    void uploadFile(@NotNull MultipartFile file, @NotNull String fileName);

    String getFileUrl(@NotNull String fileName);

}
