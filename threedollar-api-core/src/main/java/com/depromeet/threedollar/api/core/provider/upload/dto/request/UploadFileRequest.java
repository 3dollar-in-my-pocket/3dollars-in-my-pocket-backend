package com.depromeet.threedollar.api.core.provider.upload.dto.request;

import com.depromeet.threedollar.common.type.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileRequest {

    MultipartFile getFile();

    FileType getType();

    void validateAvailableUploadFile();

    default String getFileNameWithBucketDirectory(String originalFileName) {
        return getType().createUniqueFileNameWithExtension(originalFileName);
    }

}
