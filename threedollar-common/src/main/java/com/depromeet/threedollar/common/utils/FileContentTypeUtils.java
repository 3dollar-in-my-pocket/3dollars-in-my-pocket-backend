package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.common.type.FileType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.FORBIDDEN_FILE_TYPE_EXCEPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileContentTypeUtils {

    private static final String SEPARATOR = "/";

    public static void validateAvailableContentType(@Nullable String contentType, FileType fileType) {
        if (contentType != null && contentType.contains(SEPARATOR) && fileType.isAvailableContentTypePrefix(getContentTypePrefix(contentType))) {
            return;
        }
        throw new ValidationException(String.format("허용되지 않은 파일 형식 (%s) 입니다", contentType), FORBIDDEN_FILE_TYPE_EXCEPTION);
    }

    @NotNull
    private static String getContentTypePrefix(@NotNull String contentType) {
        return contentType.split(SEPARATOR)[0];
    }

}
