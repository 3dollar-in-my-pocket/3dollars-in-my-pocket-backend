package com.depromeet.threedollar.common.utils;

import com.depromeet.threedollar.common.exception.model.InternalServerException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.UnknownHostException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientIpUtils {

    /**
     * 실제 클라이언트의 IP를 가져온다.
     */
    @NotNull
    public static String getClientIp(String remoteAddress, @Nullable String forwarded) {
        if (isPublicIp(remoteAddress) || StringUtils.isBlank(forwarded)) {
            return remoteAddress;
        }
        return extractClientIP(forwarded);
    }

    private static boolean isPublicIp(String remoteAddress) {
        try {
            InetAddress ipAddress = InetAddress.getByName(remoteAddress);
            return !ipAddress.isSiteLocalAddress();
        } catch (UnknownHostException e) {
            throw new InternalServerException(String.format("알 수 없는 IP Address (%s) 입니다", remoteAddress));
        }
    }

    @NotNull
    private static String extractClientIP(@NotNull String forwarded) {
        if (forwarded.contains(",")) {
            return forwarded.split(",")[0].trim();
        }
        return forwarded.split(" ")[0].trim();
    }

}
