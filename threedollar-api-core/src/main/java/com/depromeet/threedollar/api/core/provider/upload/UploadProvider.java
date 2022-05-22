package com.depromeet.threedollar.api.core.provider.upload;

import org.springframework.stereotype.Component;

import com.depromeet.threedollar.api.core.provider.upload.dto.request.UploadFileRequest;
import com.depromeet.threedollar.external.client.storage.FileStorageClient;

import lombok.RequiredArgsConstructor;

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
