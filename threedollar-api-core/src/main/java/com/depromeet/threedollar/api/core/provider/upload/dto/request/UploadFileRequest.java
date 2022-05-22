package com.depromeet.threedollar.api.core.provider.upload.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.depromeet.threedollar.common.type.FileType;

public interface UploadFileRequest {

    MultipartFile getFile();

    FileType getType();

    void validateAvailableUploadFile();

    default String getFileNameWithBucketDirectory(String originalFileName) {
        return getType().createUniqueFileNameWithExtension(originalFileName);
    }

}
