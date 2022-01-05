package com.depromeet.threedollar.common.type;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlatformTypeTest {

    @Test
    void Android_UserAgent() {
        // given
        String userAgent = "okhttp/4.9.1";

        // when
        PlatformType platformType = PlatformType.findByUserAgent(userAgent);

        // then
        assertThat(platformType).isEqualTo(PlatformType.ANDROID);
    }

    @Test
    void Iphone_UserAgent() {
        // given
        String userAgent = "2.4.2 (com.macgongmon.-dollar-in-my-pocket; build:1; iOS 15.1.0";

        // when
        PlatformType platformType = PlatformType.findByUserAgent(userAgent);

        // then
        assertThat(platformType).isEqualTo(PlatformType.IPHONE);
    }

    @Test
    void Unknown_UserAgent() {
        // given
        String userAgent = "Unknown";

        // when
        PlatformType platformType = PlatformType.findByUserAgent(userAgent);

        // then
        assertThat(platformType).isEqualTo(PlatformType.UNKNOWN);
    }

}
