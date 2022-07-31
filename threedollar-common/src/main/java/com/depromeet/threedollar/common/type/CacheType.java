package com.depromeet.threedollar.common.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Getter
public enum CacheType {

    FAQS("FAQ 목록", CacheKey.FAQS, Duration.ofMinutes(10), CacheRange.LOCAL),
    MEDALS("전체 메달 목록", CacheKey.MEDALS, Duration.ofMinutes(10), CacheRange.LOCAL),
    ADVERTISEMENT("활성화 중인 광고 목록", CacheKey.ADVERTISEMENTS, Duration.ofMinutes(10), CacheRange.GLOBAL),
    USER_STORES_COUNTS("유저가 등록한 가게 수", CacheKey.USER_STORES_COUNTS, Duration.ofMinutes(10), CacheRange.GLOBAL),
    USER_REVIEWS_COUNTS("유저가 작성한 리뷰 수", CacheKey.USER_REVIEWS_COUNTS, Duration.ofMinutes(10), CacheRange.GLOBAL),
    USER_MEDALS("유저가 보유중인 메달 목록", CacheKey.USER_MEDALS, Duration.ofMinutes(10), CacheRange.GLOBAL),
    ;

    private final String description;
    private final String key;
    private final Duration duration;
    private final CacheRange cacheRange;

    CacheType(String description, String key, Duration duration, CacheRange cacheRange) {
        this.description = description;
        this.key = key;
        this.duration = duration;
        this.cacheRange = cacheRange;
    }

    public boolean isLocalCache() {
        return this.cacheRange == CacheRange.LOCAL;
    }

    public boolean isGlobalCache() {
        return this.cacheRange == CacheRange.GLOBAL;
    }

    private enum CacheRange {

        LOCAL,
        GLOBAL,

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CacheKey {

        public static final String FAQS = "common:v1:faqs";
        public static final String ADVERTISEMENTS = "common:v1:advertisements";

        public static final String MEDALS = "user:v1:medals";
        public static final String USER_STORES_COUNTS = "user:v1:my:stores:count";
        public static final String USER_REVIEWS_COUNTS = "user:v1:my:reviews:count";
        public static final String USER_MEDALS = "user:v1:my:medals";

    }

}
