package com.depromeet.threedollar.domain.domain.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ImageType {

    STORE("store/v2/"),
    ;

    private final String directory;

    @NotNull
    public String getFileNameWithDirectory(@NotNull String fileName) {
        return this.directory.concat(fileName);
    }

}
