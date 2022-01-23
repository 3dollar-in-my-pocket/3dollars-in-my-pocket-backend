package com.depromeet.threedollar.common.type;

import com.depromeet.threedollar.common.exception.model.ForbiddenException;
import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.common.model.EnumModel;
import com.depromeet.threedollar.common.utils.FileUtils;
import com.depromeet.threedollar.common.utils.UuidUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.FORBIDDEN_FILE_NAME_EXCEPTION;
import static com.depromeet.threedollar.common.type.ApplicationType.BOSS_API;
import static com.depromeet.threedollar.common.type.ApplicationType.USER_API;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FileType implements EnumModel {

    STORE_IMAGE("(유저) 가게 이미지", "store/v2/", FileContentType.IMAGE, List.of(USER_API)),
    STORE_CERTIFICATION_IMAGE("(사장님) 가게 인증 이미지", "boss/certification-store/v1/", FileContentType.IMAGE, List.of(BOSS_API)),
    ;

    private final String description;
    private final String directory;
    private final FileContentType contentType;
    private final List<ApplicationType> availableModules;

    public void validateAvailableUploadInModule(@NotNull ApplicationType applicationType) {
        if (!this.availableModules.contains(applicationType)) {
            throw new ForbiddenException(String.format("해당 서버 (%s) 에서 업로드할 수 없는 파일 타입 (%s) 입니다.", applicationType, this.name()));
        }
    }

    public void validateAvailableContentType(@Nullable String contentType) {
        this.contentType.validateAvailableContentType(contentType);
    }

    /**
     * 파일의 기존의 확장자를 유지한 채, 유니크한 파일의 이름을 반환합니다.
     */
    @NotNull
    public String createUniqueFileNameWithExtension(@Nullable String originalFileName) {
        if (originalFileName == null) {
            throw new ValidationException("잘못된 파일의 originFilename 입니다", FORBIDDEN_FILE_NAME_EXCEPTION);
        }
        String extension = FileUtils.getFileExtension(originalFileName);
        return getFileNameWithDirectory(UuidUtils.generate().concat(extension));
    }

    @NotNull
    private String getFileNameWithDirectory(@NotNull String fileName) {
        return this.directory.concat(fileName);
    }

    @Override
    public String getKey() {
        return name();
    }

}
