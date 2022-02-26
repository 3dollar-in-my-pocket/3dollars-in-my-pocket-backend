package com.depromeet.threedollar.application.provider.upload.dto.request;

import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.common.type.FileType;
import lombok.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUploadFileRequest implements UploadFileRequest {

    @NotNull(message = "{image.file.notNull}")
    private MultipartFile file;

    @NotNull(message = "{image.type.notNull}")
    private FileType type;

    @NotNull(message = "{image.applicationType.notNull}")
    private ApplicationType applicationType;

    public static ImageUploadFileRequest of(MultipartFile file, FileType type, ApplicationType applicationType) {
        return new ImageUploadFileRequest(file, type, applicationType);
    }

    @Override
    public void validateAvailableUploadFile() {
        type.validateAvailableContentType(file.getContentType());
        type.validateAvailableUploadInModule(applicationType);
    }

    public String getFileNameWithBucketDirectory(@Nullable String originalFileName) {
        return type.createUniqueFileNameWithExtension(originalFileName);
    }

}
