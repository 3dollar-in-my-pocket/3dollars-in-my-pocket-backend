package com.depromeet.threedollar.domain.mongo.boss.domain.store

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import com.depromeet.threedollar.common.exception.model.InvalidException

internal class BossStoreLocationTest {

    @CsvSource(
        "33.1, 124.6",
        "38.61, 131.87"
    )
    @ParameterizedTest
    fun `허용된 위도 경도 범위인경우 유효성 검증을 통과한다`(latitude: Double, longitude: Double) {
        // when & then
        assertDoesNotThrow { BossStoreLocation.of(latitude = latitude, longitude = longitude) }
    }

    @CsvSource(
        "33.09, 130.0",
        "38.62, 130.0"
    )
    @ParameterizedTest
    fun `허용된 위도 범위 밖인경우 InvalidException`(latitude: Double, longitude: Double) {
        // when & then
        assertThatThrownBy { BossStoreLocation.of(latitude = latitude, longitude = longitude) }.isInstanceOf(InvalidException::class.java)
    }

    @CsvSource(
        "35.0, 124.59",
        "35.0, 131.88"
    )
    @ParameterizedTest
    fun `허용된 경도 범위 밖인경우 InvalidException`(latitude: Double, longitude: Double) {
        // when & then
        assertThatThrownBy { BossStoreLocation.of(latitude = latitude, longitude = longitude) }.isInstanceOf(InvalidException::class.java)
    }

    @Test
    fun `위도만 달라도 위치가 변경되었다고 판단한다`() {
        // given
        val longitude = 128.0

        val bossStoreLocation = BossStoreLocation.of(latitude = 37.0, longitude = longitude)

        // when
        val result = bossStoreLocation.hasChangedLocation(latitude = 37.1, longitude = longitude)

        // then
        assertThat(result).isTrue
    }

    @Test
    fun `경도만 달라도 위치가 변경되었다고 판단한다`() {
        // given
        val latitude = 38.1

        val bossStoreLocation = BossStoreLocation.of(latitude = latitude, longitude = 128.0)

        // when
        val result = bossStoreLocation.hasChangedLocation(latitude = latitude, longitude = 128.1)

        // then
        assertThat(result).isTrue
    }

    @Test
    fun `위도 경도가 모두 같은 경우 위치가 변경되지 않았다고 판단한다`() {
        // given
        val latitude = 38.1
        val longitude = 128.0

        val bossStoreLocation = BossStoreLocation.of(latitude = latitude, longitude = longitude)

        // when
        val result = bossStoreLocation.hasChangedLocation(latitude = latitude, longitude = longitude)

        // then
        assertThat(result).isFalse
    }

}
