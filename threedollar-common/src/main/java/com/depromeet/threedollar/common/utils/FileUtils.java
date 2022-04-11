package com.depromeet.threedollar.common.utils;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_UPLOAD_FILE_TYPE;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.common.exception.model.InvalidException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    /**
     * 파일의 확장자를 반환합니다.
     * 잘못된 파일의 확장자인경우 throws ValidationException
     *
     * @param fileName ex) image.png
     * @return ex) .png
     */
    public static String getFileExtension(@NotNull String fileName) {
        try {
            String extension = fileName.substring(fileName.lastIndexOf("."));
            if (extension.length() < 2) {
                throw new InvalidException(String.format("잘못된 확장자 형식의 파일 (%s) 입니다", fileName), INVALID_UPLOAD_FILE_TYPE);
            }
            return extension;
        } catch (StringIndexOutOfBoundsException e) {
            throw new InvalidException(String.format("잘못된 확장자 형식의 파일 (%s) 입니다", fileName), INVALID_UPLOAD_FILE_TYPE);
        }
    }

}
