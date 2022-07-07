package com.depromeet.threedollar.infrastructure.s3.provider.dto.request;

import com.depromeet.threedollar.infrastructure.s3.common.type.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileRequest {

    MultipartFile getFile();

    FileType getType();

    void validateAvailableUploadFile();

    default String getFileNameWithBucketDirectory(String originalFileName) {
        return getType().createUniqueFileNameWithExtension(originalFileName);
    }

}
