package com.depromeet.threedollar.external.client.storage;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Primary
@Profile({"local", "local-docker", "integration-test"})
@Component
public class DummyFileStorageClient implements FileStorageClient {

    @Override
    public void uploadFile(@NotNull MultipartFile file, @NotNull String fileName) {
        log.info("파일이 더미 버킷으로 업로드 되었습니다. {}", file.getOriginalFilename());
    }

    @Override
    public String getFileUrl(@NotNull String fileName) {
        return fileName;
    }

}
