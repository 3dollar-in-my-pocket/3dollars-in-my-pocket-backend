package com.depromeet.threedollar.domain.mongo.domain.commonservice.device

import java.util.stream.Stream
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.type.OsPlatformType

internal class DeviceInfoTest {

    @MethodSource("supportedPushPlatformAndOsPlatform")
    @ParameterizedTest
    fun `해당하는 OS에서 지원하는 푸시 플랫폼인경우 에러가 발생하지 않는다`(pushPlatformType: PushPlatformType, osPlatformType: OsPlatformType) {
        // when & then
        assertDoesNotThrow {
            DeviceInfo.of(
                pushPlatformType = pushPlatformType,
                osPlatformType = osPlatformType,
                pushToken = "push-token",
                appVersion = "1.0.0"
            )
        }
    }

    @MethodSource("notSupportedPushPlatformAndOsPlatform")
    @ParameterizedTest
    @Test
    fun `해당하는 OS에서 지원하지 않는 푸시 플랫폼인경우 Forbidden 에러가 발생한다`() {
        // given
        val pushPlatformType = PushPlatformType.FCM
        val osPlatformType = OsPlatformType.UNKNOWN

        // when & then
        assertThatThrownBy {
            DeviceInfo.of(
                pushPlatformType = pushPlatformType,
                osPlatformType = osPlatformType,
                pushToken = "push-token",
                appVersion = "1.0.0"
            )
        }.isInstanceOf(ForbiddenException::class.java)
    }

    companion object {
        @JvmStatic
        private fun supportedPushPlatformAndOsPlatform(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(PushPlatformType.FCM, OsPlatformType.IPHONE),
                Arguments.of(PushPlatformType.FCM, OsPlatformType.ANDROID),
            )
        }

        @JvmStatic
        private fun notSupportedPushPlatformAndOsPlatform(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(PushPlatformType.FCM, OsPlatformType.UNKNOWN),
            )
        }
    }

}
