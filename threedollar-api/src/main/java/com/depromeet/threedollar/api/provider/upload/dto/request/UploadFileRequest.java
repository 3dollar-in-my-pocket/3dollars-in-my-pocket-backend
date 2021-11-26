package com.depromeet.threedollar.api.provider.upload.dto.request;

public interface UploadFileRequest {

    void validateAvailableFileType(String contentType);

    String createFileName(String originalFileName);

}
