package com.depromeet.threedollar.external.client.apple;

import com.depromeet.threedollar.common.exception.model.ValidationException;
import com.depromeet.threedollar.external.client.apple.dto.properties.AppleAuthProperties;
import com.depromeet.threedollar.external.client.apple.dto.response.ApplePublicKeyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

import static com.depromeet.threedollar.common.exception.ErrorCode.*;

/**
 * https://developer.apple.com/documentation/sign_in_with_apple/sign_in_with_apple_rest_api/verifying_a_user
 */
@RequiredArgsConstructor
@Component
public class AppleTokenDecoderImpl implements AppleTokenDecoder {

    private final AppleApiClient appleApiCaller;
    private final AppleAuthProperties appleAuthProperties;
    private final ObjectMapper objectMapper;

    @Override
    public String getSocialIdFromIdToken(String idToken) {
        String headerIdToken = idToken.split("\\.")[0];
        try {
            Map<String, String> header = objectMapper.readValue(new String(Base64.getDecoder().decode(headerIdToken), StandardCharsets.UTF_8), new TypeReference<>() {
            });
            PublicKey publicKey = getPublicKey(header);
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .requireIssuer(appleAuthProperties.getIssuer())
                .requireAudience(appleAuthProperties.getClientId())
                .build()
                .parseClaimsJws(idToken)
                .getBody();
            return claims.getSubject();
        } catch (JsonProcessingException | InvalidKeySpecException | InvalidClaimException | NoSuchAlgorithmException e) {
            throw new ValidationException(String.format("잘못된 애플 idToken (%s) 입니다 (reason: %s)", idToken, e.getMessage()), VALIDATION_APPLE_TOKEN_EXCEPTION);
        } catch (ExpiredJwtException e) {
            throw new ValidationException(String.format("만료된 애플 idToken (%s) 입니다 (reason: %s)", idToken, e.getMessage()), VALIDATION_APPLE_TOKEN_EXPIRED_EXCEPTION);
        }
    }

    private PublicKey getPublicKey(Map<String, String> header) throws InvalidKeySpecException, NoSuchAlgorithmException {
        ApplePublicKeyResponse response = appleApiCaller.getAppleAuthPublicKey();
        ApplePublicKeyResponse.Key key = response.getMatchedPublicKey(header.get("kid"), header.get("alg"));

        byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
        return keyFactory.generatePublic(publicKeySpec);
    }

}
