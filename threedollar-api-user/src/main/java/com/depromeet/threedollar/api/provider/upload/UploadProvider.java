package com.depromeet.threedollar.api.provider.upload;

import com.depromeet.threedollar.api.provider.upload.dto.request.UploadFileRequest;
import com.depromeet.threedollar.external.client.s3.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class UploadProvider {

    private final S3Client s3Client;

    public String uploadFile(UploadFileRequest request, MultipartFile file) {
        request.validateAvailableFileType(file.getContentType());
        String fileName = request.createFileName(file.getOriginalFilename());
        s3Client.uploadFile(file, fileName);
        return s3Client.getFileUrl(fileName);
    }

}
