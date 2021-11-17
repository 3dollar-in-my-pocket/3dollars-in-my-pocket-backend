package com.depromeet.threedollar.api.service.upload.dto.request;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.domain.domain.common.ImageType;
import com.depromeet.threedollar.common.utils.FileUtils;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static com.depromeet.threedollar.common.exception.ErrorCode.VALIDATION_FILE_TYPE_EXCEPTION;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUploadRequest implements UploadRequest {

    private static final String SEPARATOR = "/";
    private static final String IMAGE_CONTENT_TYPE_TYPE = "image";

    @NotNull(message = "{image.type.notNull}")
    private ImageType type;

    public static ImageUploadRequest of(ImageType type) {
        return new ImageUploadRequest(type);
    }

    @Override
    public void validateAvailableFileType(String contentType) {
        if (contentType != null && contentType.contains(SEPARATOR) && contentType.split(SEPARATOR)[0].equals(IMAGE_CONTENT_TYPE_TYPE)) {
            return;
        }
        throw new ValidationException(String.format("허용되지 않은 파일 형식 (%s) 입니다", contentType), VALIDATION_FILE_TYPE_EXCEPTION);
    }

    @Override
    public String createFileName(String originalFileName) {
        String extension = FileUtils.getFileExtension(originalFileName);
        return type.getFileNameWithDirectory(UUID.randomUUID().toString().concat(extension));
    }

}
