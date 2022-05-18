package com.depromeet.threedollar.domain.mongo.boss.domain.store

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import com.depromeet.threedollar.common.exception.model.InvalidException

internal class BossStoreLocationTest {

    @Test
    fun `가게의 위치 정보를 생성할떄 Vadliation 체크가 진핻된다`() {
        // given
        val latitude = -1.0
        val longitude = -1.0

        // when & then
        assertThatThrownBy { BossStoreLocation.of(bossStoreId = "bossStoreId", latitude = latitude, longitude) }.isInstanceOf(InvalidException::class.java)
    }

    @Test
    fun `가게의 위치 정보를 수정할때 Vadliation 체크가 진행된다`() {
        // given
        val latitude = -1.0
        val longitude = -1.0

        val bossStoreLocation = BossStoreLocationCreator.create(bossStoreId = "bossStoreId", latitude = 38.0, longitude = 128.0)

        // when & then
        assertThatThrownBy { bossStoreLocation.updateLocation(latitude = latitude, longitude = longitude) }.isInstanceOf(InvalidException::class.java)
    }

}
