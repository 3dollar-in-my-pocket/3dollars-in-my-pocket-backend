package com.depromeet.threedollar.external.client.apple;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.INVALID_AUTH_TOKEN;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.external.client.apple.dto.property.AppleAuthProperty;
import com.depromeet.threedollar.external.client.apple.dto.response.ApplePublicKeyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

/**
 * <a href="https://developer.apple.com/documentation/sign_in_with_apple/sign_in_with_apple_rest_api/verifying_a_user">https://developer.apple.com/documentation/sign_in_with_apple/sign_in_with_apple_rest_api/verifying_a_user</a>
 */
@RequiredArgsConstructor
@Component
public class AppleTokenDecoderImpl implements AppleTokenDecoder {

    private final AppleAuthApiClient appleApiCaller;
    private final AppleAuthProperty appleAuthProperty;
    private final ObjectMapper objectMapper;

    @Override
    public String getSocialIdFromIdToken(@NotNull String idToken) {
        String headerIdToken = idToken.split("\\.")[0];
        try {
            Map<String, String> header = objectMapper.readValue(new String(Base64.getDecoder().decode(headerIdToken), StandardCharsets.UTF_8), new TypeReference<>() {
            });
            PublicKey publicKey = getPublicKey(header);
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .requireIssuer(appleAuthProperty.getIssuer())
                .requireAudience(appleAuthProperty.getClientId())
                .build()
                .parseClaimsJws(idToken)
                .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new InvalidException(String.format("만료된 애플 idToken(%s) 입니다 (reason: %s)", idToken, e.getMessage()), INVALID_AUTH_TOKEN);
        } catch (JsonProcessingException | InvalidKeySpecException | InvalidClaimException | NoSuchAlgorithmException |
                 IllegalArgumentException e) {
            throw new InvalidException(String.format("잘못된 애플 idToken(%s) 입니다 (reason: %s)", idToken, e.getMessage()), INVALID_AUTH_TOKEN);
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
