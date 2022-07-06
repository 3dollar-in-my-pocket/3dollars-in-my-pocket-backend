package com.depromeet.threedollar.infrastructure.s3.provider.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.depromeet.threedollar.infrastructure.s3.common.type.FileType;

public interface UploadFileRequest {

    MultipartFile getFile();

    FileType getType();

    void validateAvailableUploadFile();

    default String getFileNameWithBucketDirectory(String originalFileName) {
        return getType().createUniqueFileNameWithExtension(originalFileName);
    }

}
