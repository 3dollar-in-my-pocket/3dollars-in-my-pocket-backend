package com.depromeet.threedollar.api.provider.upload.dto.request;

import org.jetbrains.annotations.Nullable;

public interface UploadFileRequest {

    void validateAvailableFileType(@Nullable String contentType);

    String createFileName(@Nullable String originalFileName);

}
