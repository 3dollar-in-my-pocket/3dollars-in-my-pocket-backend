package com.depromeet.threedollar.api.provider.upload;

import com.depromeet.threedollar.api.provider.upload.dto.request.UploadFileRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UploadProvider {

    String uploadFile(UploadFileRequest request, MultipartFile file);

}
