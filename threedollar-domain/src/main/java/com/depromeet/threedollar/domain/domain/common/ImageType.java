package com.depromeet.threedollar.domain.domain.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ImageType {

    STORE("store/v2/"),
    ;

    private final String directory;

    public String getFileNameWithDirectory(String fileName) {
        return this.directory.concat(fileName);
    }

}
