package com.depromeet.threedollar.common.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PlatformType {

    IPHONE(List.of("ios")),
    ANDROID(List.of("okhttp")),
    UNKNOWN(Collections.emptyList()),
    ;

    private final List<String> keywords;

    private boolean hasKeyword(String userAgent) {
        return this.keywords.stream()
            .anyMatch(keyword -> StringUtils.containsIgnoreCase(userAgent, keyword));
    }

    @NotNull
    public static PlatformType findByUserAgent(String userAgent) {
        return Arrays.stream(PlatformType.values())
            .filter(platformType -> platformType.hasKeyword(userAgent))
            .findFirst()
            .orElse(PlatformType.UNKNOWN);
    }

}
