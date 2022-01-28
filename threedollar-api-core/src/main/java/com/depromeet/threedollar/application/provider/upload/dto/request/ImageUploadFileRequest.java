package com.depromeet.threedollar.application.provider.upload.dto.request;

import com.depromeet.threedollar.common.type.FileType;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUploadFileRequest implements UploadFileRequest {

    private static final String SEPARATOR = "/";
    private static final String IMAGE_CONTENT_TYPE_TYPE = "image";

    @NotNull(message = "{image.type.notNull}")
    private FileType type;

    public static ImageUploadFileRequest of(FileType type) {
        return new ImageUploadFileRequest(type);
    }

    public String getFileNameWithBucketDirectory(@Nullable String originalFileName) {
        return type.createUniqueFileNameWithExtension(originalFileName);
    }

}
