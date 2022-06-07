package com.depromeet.threedollar.common.utils;

import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpHeaderUtils {

    private static final String BEARER_TOKEN = "Bearer ";

    @NotNull
    public static String withBearerToken(@NotNull String token) {
        return BEARER_TOKEN + token;
    }

}
