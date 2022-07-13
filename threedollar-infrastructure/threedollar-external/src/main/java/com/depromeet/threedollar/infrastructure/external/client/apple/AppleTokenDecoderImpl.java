package com.depromeet.threedollar.infrastructure.external.client.apple;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import com.depromeet.threedollar.common.utils.LocalDateTimeUtils;
import com.depromeet.threedollar.infrastructure.external.client.apple.dto.model.AppleIdTokenPayload;
import com.depromeet.threedollar.infrastructure.external.client.apple.dto.property.AppleAuthProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.E400_INVALID_AUTH_TOKEN;

/**
 * <a href="https://developer.apple.com/documentation/sign_in_with_apple/sign_in_with_apple_rest_api/verifying_a_user">https://developer.apple.com/documentation/sign_in_with_apple/sign_in_with_apple_rest_api/verifying_a_user</a>
 */
@RequiredArgsConstructor
@Component
public class AppleTokenDecoderImpl implements AppleTokenDecoder {

    /**
     * 애플 토큰에서 제공되는 만료시간 (1일)을 사용하지 않고, 발급시간 후 5분을 자체 만료시간으로 사용한다.
     */
    private static final Duration EXPIRED_DURATION = Duration.ofMinutes(5);

    private final AppleAuthProperty appleAuthProperty;
    private final ObjectMapper objectMapper;

    @Override
    public String getSocialIdFromIdToken(@NotNull String idToken) {
        try {
            String payload = idToken.split("\\.")[1];
            String decodedPayload = new String(Base64.getDecoder().decode(payload));
            AppleIdTokenPayload appleIdTokenPayload = objectMapper.readValue(decodedPayload, AppleIdTokenPayload.class);
            validateToken(appleIdTokenPayload);
            return appleIdTokenPayload.getSub();
        } catch (IOException | IllegalArgumentException e) {
            throw new InvalidException(String.format("잘못된 토큰 (%s) 입니다", idToken), ErrorCode.E400_INVALID_AUTH_TOKEN);
        }
    }

    private void validateToken(@NotNull AppleIdTokenPayload payload) {
        if (!payload.getIss().equals(appleAuthProperty.getIssuer())) {
            throw new InvalidException(String.format("잘못된 애플 토큰 입니다 - issuer가 일치하지 않습니다 payload: (%s)", payload), E400_INVALID_AUTH_TOKEN);
        }
        if (!payload.getAud().equals(appleAuthProperty.getClientId())) {
            throw new InvalidException(String.format("잘못된 애플 토큰 입니다 - clientId가 일치하지 않습니다 payload: (%s)", payload), E400_INVALID_AUTH_TOKEN);
        }
        LocalDateTime authDateTime = LocalDateTimeUtils.epochToLocalDateTime(payload.getAuthTime());
        if (authDateTime.plus(EXPIRED_DURATION).isBefore(LocalDateTime.now())) {
            throw new InvalidException(String.format("발급 후 5분애플 토큰 (%s) 입니다 만료시간: (%s)", payload, authDateTime), E400_INVALID_AUTH_TOKEN);
        }
    }

}
