package com.depromeet.threedollar.external.client.apple.dto.response;

import java.util.List;

import com.depromeet.threedollar.common.exception.model.InvalidException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * GET Apple Public Key
 * <a href="https://developer.apple.com/documentation/sign_in_with_apple/jwkset/keys">https://developer.apple.com/documentation/sign_in_with_apple/jwkset/keys</a>
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
            .orElseThrow(() -> new InvalidException("일치하는 Public Key가 존재하지 않습니다"));
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
