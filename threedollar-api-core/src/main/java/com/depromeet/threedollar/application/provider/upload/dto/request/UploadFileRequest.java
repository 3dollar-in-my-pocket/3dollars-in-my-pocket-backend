package com.depromeet.threedollar.application.provider.upload.dto.request;

import com.depromeet.threedollar.common.type.FileType;

public interface UploadFileRequest {

    FileType getType();

    default String getFileNameWithBucketDirectory(String originalFileName) {
        return getType().createUniqueFileNameWithExtension(originalFileName);
    }

}
