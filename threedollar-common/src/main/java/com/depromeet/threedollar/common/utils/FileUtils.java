package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.exception.ErrorCode;
import com.depromeet.threedollar.common.exception.ValidationException;
import com.depromeet.threedollar.common.utils.type.ImageType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    private static final String IMAGE_CONTENT_TYPE_TYPE = "image";
    private static final String SEPARATOR = "/";

    public static String createFileUuidNameWithExtension(ImageType type, String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return type.getFileNameWithDirectory(UUID.randomUUID().toString().concat(extension));
    }

    private static String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ValidationException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName), ErrorCode.VALIDATION_FILE_TYPE_EXCEPTION);
        }
    }

    public static void validateImageFile(String contentType) {
        if (contentType.contains(SEPARATOR) && contentType.split(SEPARATOR)[0].equals(IMAGE_CONTENT_TYPE_TYPE)) {
            return;
        }
        throw new ValidationException(String.format("허용되지 않은 파일 형식 (%s) 입니다", contentType), ErrorCode.VALIDATION_FILE_TYPE_EXCEPTION);
    }

}
