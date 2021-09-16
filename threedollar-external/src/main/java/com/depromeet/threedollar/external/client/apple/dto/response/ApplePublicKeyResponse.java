package com.depromeet.threedollar.external.client.apple.dto.response;

import com.depromeet.threedollar.common.exception.ValidationException;
import lombok.*;

import java.util.List;

/**
 * GET Apple Public Key
 * https://developer.apple.com/documentation/sign_in_with_apple/jwkset/keys
 */
@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplePublicKeyResponse {

    private List<Key> keys;

    public Key getMatchedPublicKey(String kid, String alg) {
        return keys.stream()
            .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
            .findFirst()
            .orElseThrow(() -> new ValidationException("일치하는 Public Key가 존재하지 않습니다"));
    }

    @ToString
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Key {
        private String alg;
        private String e;
        private String kid;
        private String kty;
        private String n;
        private String use;
    }

}
