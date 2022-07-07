package com.depromeet.threedollar.infrastructure.s3.common.type;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_UPLOAD_FILE_TYPE;

@Getter
public enum FileContentType {

    IMAGE("image"),
    ;

    private static final String SEPARATOR = "/";
    private final String prefix;

    FileContentType(String prefix) {
        this.prefix = prefix;
    }

    @NotNull
    private static String getContentTypePrefix(@NotNull String contentType) {
        return contentType.split(SEPARATOR)[0];
    }

    public void validateAvailableContentType(@Nullable String contentType) {
        if (contentType != null && contentType.contains(SEPARATOR) && prefix.equals(getContentTypePrefix(contentType))) {
            return;
        }
        throw new InvalidException(String.format("허용되지 않은 ContentType (%s) 입니다", contentType), INVALID_UPLOAD_FILE_TYPE);
    }

}
