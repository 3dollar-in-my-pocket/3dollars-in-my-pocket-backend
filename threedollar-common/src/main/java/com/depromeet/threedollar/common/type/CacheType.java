package com.depromeet.threedollar.common.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Getter
public enum CacheType {

    FAQS("FAQ 목록", CacheKey.FAQS, Duration.ofHours(1), CacheRange.GLOBAL),
    MEDALS("전체 메달 목록", CacheKey.MEDALS, Duration.ofHours(1), CacheRange.GLOBAL),
    ADVERTISEMENT("활성화 중인 광고 목록", CacheKey.ADVERTISEMENTS, Duration.ofMinutes(10), CacheRange.GLOBAL),
    USER_STORES_COUNTS("유저가 등록한 가게 수", CacheKey.USER_STORES_COUNTS, Duration.ofMinutes(10), CacheRange.GLOBAL),
    USER_REVIEWS_COUNTS("유저가 작성한 리뷰 수", CacheKey.USER_REVIEWS_COUNTS, Duration.ofMinutes(10), CacheRange.GLOBAL),
    USER_MEDALS("유저가 보유중인 메달 목록", CacheKey.USER_MEDALS, Duration.ofMinutes(10), CacheRange.GLOBAL),
    BOSS_STORE_CATEGORIES("사장님 가게의 카테고리 목록", CacheKey.BOSS_STORE_CATEGORIES, Duration.ofHours(1), CacheRange.GLOBAL),
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

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CacheKey {

        public static final String FAQS = "faqs";
        public static final String MEDALS = "medals";
        public static final String ADVERTISEMENTS = "advertisements";

        public static final String USER_STORES_COUNTS = "user_stores_count";
        public static final String USER_REVIEWS_COUNTS = "user_reviews_count";
        public static final String USER_MEDALS = "user_medals";

        public static final String BOSS_STORE_CATEGORIES = "boss_store_categories";

    }

    private enum CacheRange {

        LOCAL,
        GLOBAL,

    }

    public boolean isLocalCache() {
        return this.cacheRange == CacheRange.LOCAL;
    }

    public boolean isGlobalCache() {
        return this.cacheRange == CacheRange.GLOBAL;
    }

}
