package com.depromeet.threedollar.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientIpUtils {

    /**
     * 실제 클라이언트의 IP를 가져온다.
     */
    public static String getClientIp(String remoteAddress, @Nullable String forwarded) {
        if (forwarded == null || forwarded.isBlank()) {
            return remoteAddress;
        }
        return extractClientIP(forwarded);
    }

    private static String extractClientIP(@NotNull String forwarded) {
        if (forwarded.contains(",")) {
            return forwarded.split(",")[0].trim();
        }
        return forwarded.split(" ")[0].trim();
    }

}
