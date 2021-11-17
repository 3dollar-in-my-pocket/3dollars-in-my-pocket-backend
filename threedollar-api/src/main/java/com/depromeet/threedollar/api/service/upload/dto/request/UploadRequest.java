package com.depromeet.threedollar.api.service.upload.dto.request;

public interface UploadRequest {

    void validateAvailableFileType(String contentType);

    String createFileName(String originalFileName);

}
