package com.depromeet.threedollar.infrastructure.s3.common.type;

import com.depromeet.threedollar.common.exception.model.ForbiddenException;
import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import com.depromeet.threedollar.common.model.EnumModel;
import com.depromeet.threedollar.common.type.ApplicationType;
import com.depromeet.threedollar.common.utils.FileUtils;
import com.depromeet.threedollar.common.utils.UuidUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_EMPTY_UPLOAD_FILE_NAME;
import static com.depromeet.threedollar.common.type.ApplicationType.ADMIN_API;
import static com.depromeet.threedollar.common.type.ApplicationType.BOSS_API;
import static com.depromeet.threedollar.common.type.ApplicationType.USER_API;

@Getter
public enum FileType implements EnumModel {

    ADVERTISEMENT_IMAGE("(유저) 광고 이미지", "popup/", FileContentType.IMAGE, List.of(ADMIN_API)),
    MEDAL_IMAGE("(유저) 메달 이미지", "medal/", FileContentType.IMAGE, List.of(ADMIN_API)),
    STORE_IMAGE("(유저) 가게 이미지", "store/v2/", FileContentType.IMAGE, List.of(USER_API)),
    BOSS_STORE_CERTIFICATION_IMAGE("(사장님) 가게 인증용 이미지", "boss/store-certification/v1/", FileContentType.IMAGE, List.of(BOSS_API)),
    BOSS_STORE_IMAGE("(사장님) 가게 이미지", "boss/store/v1/", FileContentType.IMAGE, List.of(BOSS_API)),
    BOSS_STORE_MENU_IMAGE("(사장님) 가게 메뉴 이미지", "boss/store-menu/v1/", FileContentType.IMAGE, List.of(BOSS_API)),
    ;

    private final String description;
    private final String directory;
    private final FileContentType contentType;
    private final List<ApplicationType> availableModules;

    FileType(String description, String directory, FileContentType contentType, List<ApplicationType> availableModules) {
        this.description = description;
        this.directory = directory;
        this.contentType = contentType;
        this.availableModules = availableModules;
    }

    public void validateAvailableUploadInModule(@NotNull ApplicationType applicationType) {
        if (!this.availableModules.contains(applicationType)) {
            throw new ForbiddenException(String.format("해당 서버 (%s) 에서 업로드할 수 없는 파일 타입 (%s) 입니다.", applicationType, this.name()), ErrorCode.NOT_IMPLEMENTED_UPLOAD_FILE_IN_MODULE);
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
            throw new InvalidException("파일의 이름이 null 이어서는 안됩니다", INVALID_EMPTY_UPLOAD_FILE_NAME);
        }
        String extension = FileUtils.getFileExtension(originalFileName);
        return getFileNameWithDirectory(UuidUtils.generate() + extension);
    }

    @NotNull
    private String getFileNameWithDirectory(@NotNull String fileName) {
        return this.directory + fileName;
    }

    @Override
    public String getKey() {
        return name();
    }

}
