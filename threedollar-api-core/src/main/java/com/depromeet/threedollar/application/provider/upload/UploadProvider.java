package com.depromeet.threedollar.application.provider.upload;

import com.depromeet.threedollar.application.provider.upload.dto.request.UploadFileRequest;
import com.depromeet.threedollar.external.client.storage.FileStorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UploadProvider {

    private final FileStorageClient fileStorageClient;

    public String uploadFile(UploadFileRequest request) {
        request.validateAvailableUploadFile();
        String fileName = request.getFileNameWithBucketDirectory(request.getFile().getOriginalFilename());
        fileStorageClient.uploadFile(request.getFile(), fileName);
        return fileStorageClient.getFileUrl(fileName);
    }

}
