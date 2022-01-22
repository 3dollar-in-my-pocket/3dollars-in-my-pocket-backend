package com.depromeet.threedollar.application.provider.upload;

import com.depromeet.threedollar.application.provider.upload.dto.request.UploadFileRequest;
import com.depromeet.threedollar.external.client.filestorage.FileStorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class UploadProvider {

    private final FileStorageClient fileStorageClient;

    public String uploadFile(UploadFileRequest request, MultipartFile file) {
        request.validateAvailableFileType(file.getContentType());
        String fileName = request.createFileName(file.getOriginalFilename());
        fileStorageClient.uploadFile(file, fileName);
        return fileStorageClient.getFileUrl(fileName);
    }

}
