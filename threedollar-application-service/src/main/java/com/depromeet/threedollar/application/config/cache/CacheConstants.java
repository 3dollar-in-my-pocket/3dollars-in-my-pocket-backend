package com.depromeet.threedollar.application.config.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public enum CacheConstants {

    FAQS(CacheKeyName.FAQS, Duration.ofHours(1));

    private final String key;
    private final Duration duration;

    public static class CacheKeyName {

        public static final String FAQS = "FAQS";

    }

}
