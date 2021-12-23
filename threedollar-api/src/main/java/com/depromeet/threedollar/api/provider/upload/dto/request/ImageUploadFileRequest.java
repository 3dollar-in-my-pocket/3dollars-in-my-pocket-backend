package com.depromeet.threedollar.api.provider.upload.dto.request;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.common.utils.FileUtils;
import com.depromeet.threedollar.domain.domain.common.ImageType;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static com.depromeet.threedollar.common.exception.ErrorCode.FORBIDDEN_FILE_NAME_EXCEPTION;
import static com.depromeet.threedollar.common.exception.ErrorCode.FORBIDDEN_FILE_TYPE_EXCEPTION;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUploadFileRequest implements UploadFileRequest {

    private static final String SEPARATOR = "/";
    private static final String IMAGE_CONTENT_TYPE_TYPE = "image";

    @NotNull(message = "{image.type.notNull}")
    private ImageType type;

    public static ImageUploadFileRequest of(ImageType type) {
        return new ImageUploadFileRequest(type);
    }

    @Override
    public void validateAvailableFileType(@Nullable String contentType) {
        if (contentType != null && contentType.contains(SEPARATOR) && IMAGE_CONTENT_TYPE_TYPE.equals(contentType.split(SEPARATOR)[0])) {
            return;
        }
        throw new ValidationException(String.format("허용되지 않은 파일 형식 (%s) 입니다", contentType), FORBIDDEN_FILE_TYPE_EXCEPTION);
    }

    @Override
    public String createFileName(@Nullable String originalFileName) {
        if (originalFileName == null) {
            throw new ValidationException("잘못된 파일의 originFilename 입니다", FORBIDDEN_FILE_NAME_EXCEPTION);
        }
        String extension = FileUtils.getFileExtension(originalFileName);
        return type.getFileNameWithDirectory(UUID.randomUUID().toString().concat(extension));
    }

}
