package com.depromeet.threedollar.domain.config.cache;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CacheType {

    FAQS(CacheKey.FAQS, Duration.ofHours(1)),
    POPUP(CacheKey.POPUP, Duration.ofHours(1)),
    USER_STORES_COUNTS(CacheKey.USER_STORES_COUNTS, Duration.ofMinutes(15)),
    USER_REVIEWS_COUNTS(CacheKey.USER_REVIEWS_COUNTS, Duration.ofMinutes(15)),
    MEDALS(CacheKey.MEDALS, Duration.ofHours(1)),
    USER_MEDALS(CacheKey.USER_MEDALS, Duration.ofMinutes(15)),
    ;

    private final String key;
    private final Duration duration;

    public static class CacheKey {

        public static final String FAQS = "FAQS";
        public static final String POPUP = "POPUP";
        public static final String USER_STORES_COUNTS = "USER_STORES_COUNTS";
        public static final String USER_REVIEWS_COUNTS = "USER_REVIEWS_COUNTS";
        public static final String MEDALS = "MEDALS";
        public static final String USER_MEDALS = "USER_MEDALS";

    }

}
