package com.depromeet.threedollar.common.type;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OsPlatformTypeTest {

    @Test
    void Android_UserAgent() {
        // given
        String userAgent = "okhttp/4.9.1";

        // when
        OsPlatformType osPlatformType = OsPlatformType.findByUserAgent(userAgent);

        // then
        assertThat(osPlatformType).isEqualTo(OsPlatformType.ANDROID);
    }

    @Test
    void Iphone_UserAgent() {
        // given
        String userAgent = "2.4.2 (com.macgongmon.-dollar-in-my-pocket; build:1; iOS 15.1.0";

        // when
        OsPlatformType osPlatformType = OsPlatformType.findByUserAgent(userAgent);

        // then
        assertThat(osPlatformType).isEqualTo(OsPlatformType.IPHONE);
    }

    @Test
    void Unknown_UserAgent() {
        // given
        String userAgent = "Unknown";

        // when
        OsPlatformType osPlatformType = OsPlatformType.findByUserAgent(userAgent);

        // then
        assertThat(osPlatformType).isEqualTo(OsPlatformType.UNKNOWN);
    }

}
