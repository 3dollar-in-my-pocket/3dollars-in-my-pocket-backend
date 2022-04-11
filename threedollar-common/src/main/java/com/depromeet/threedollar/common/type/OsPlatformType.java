package com.depromeet.threedollar.common.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public enum OsPlatformType {

    IPHONE(List.of("ios")),
    ANDROID(List.of("okhttp")),
    UNKNOWN(Collections.emptyList()),
    ;

    private final List<String> keywords;

    OsPlatformType(List<String> keywords) {
        this.keywords = keywords;
    }

    @NotNull
    public static OsPlatformType findByUserAgent(String userAgent) {
        return Arrays.stream(OsPlatformType.values())
            .filter(osPlatformType -> osPlatformType.hasKeyword(userAgent))
            .findFirst()
            .orElse(OsPlatformType.UNKNOWN);
    }

    private boolean hasKeyword(String userAgent) {
        return this.keywords.stream()
            .anyMatch(keyword -> StringUtils.containsIgnoreCase(userAgent, keyword));
    }

}
