package com.depromeet.threedollar.application.config.cache;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CacheType {

    FAQS(CacheKey.FAQS, Duration.ofHours(1)),
    POPUP(CacheKey.POPUP, Duration.ofHours(1)),
    ;

    private final String key;
    private final Duration duration;

    public static class CacheKey {

        public static final String FAQS = "FAQS";
        public static final String POPUP = "POPUP";

    }

}
