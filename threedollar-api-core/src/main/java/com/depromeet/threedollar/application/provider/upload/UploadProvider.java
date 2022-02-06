package com.depromeet.threedollar.application.provider.upload;

import com.depromeet.threedollar.application.provider.upload.dto.request.UploadFileRequest;
import com.depromeet.threedollar.external.client.storage.FileStorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class UploadProvider {

    private final FileStorageClient fileStorageClient;

    public String uploadFile(UploadFileRequest request, MultipartFile file) {
        request.validateAvailableContentType(file.getContentType());
        String fileName = request.getFileNameWithBucketDirectory(file.getOriginalFilename());
        fileStorageClient.uploadFile(file, fileName);
        return fileStorageClient.getFileUrl(fileName);
    }

}
