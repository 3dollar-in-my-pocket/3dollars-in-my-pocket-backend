package com.depromeet.threedollar.common.type;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.FORBIDDEN_FILE_TYPE_EXCEPTION;

@Getter
public enum FileContentType {

    IMAGE("image"),
    ;

    private final String prefix;

    private FileContentType(String prefix) {
        this.prefix = prefix;
    }

    public void validateAvailableContentType(@Nullable String contentType) {
        if (contentType != null && contentType.contains(SEPARATOR) && prefix.equals(getContentTypePrefix(contentType))) {
            return;
        }
        throw new ValidationException(String.format("허용되지 않은 파일 형식 (%s) 입니다", contentType), FORBIDDEN_FILE_TYPE_EXCEPTION);
    }

    @NotNull
    private static String getContentTypePrefix(@NotNull String contentType) {
        return contentType.split(SEPARATOR)[0];
    }

    private static final String SEPARATOR = "/";

}
