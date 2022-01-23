package com.depromeet.threedollar.application.provider.upload;

import com.depromeet.threedollar.application.provider.upload.dto.request.UploadFileRequest;
import com.depromeet.threedollar.common.utils.FileContentTypeUtils;
import com.depromeet.threedollar.external.client.filestorage.FileStorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class UploadProvider {

    private final FileStorageClient fileStorageClient;

    public String uploadFile(UploadFileRequest request, MultipartFile file) {
        FileContentTypeUtils.validateAvailableContentType(file.getContentType(), request.getType());
        String fileName = request.getFileNameWithBucketDirectory(file.getOriginalFilename());
        fileStorageClient.uploadFile(file, fileName);
        return fileStorageClient.getFileUrl(fileName);
    }

}
