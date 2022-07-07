package com.depromeet.threedollar.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpHeaderUtils {

    private static final String BEARER_TOKEN = "Bearer ";

    @NotNull
    public static String withBearerToken(@NotNull String token) {
        return BEARER_TOKEN + token;
    }

}
