package com.depromeet.threedollar.api.bossservice.service.store

import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import com.depromeet.threedollar.api.bossservice.service.SetupBossStoreServiceTest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.redis.domain.bossservice.store.BossStoreOpenTimeRepository

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class BossStoreOpenServiceTest(
    private val bossStoreOpenService: BossStoreOpenService,
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
) : SetupBossStoreServiceTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreRepository.deleteAll()
    }

    @Test
    fun `기존의 오픈 정보가 없는경우 현재시간으로 오픈정보가 레디스에 추가된다`() {
        // when
        bossStoreOpenService.openBossStore(
            bossStoreId = bossStoreId,
            bossId = bossId,
            mapLocation = LocationValue.of(38.0, 127.0)
        )

        // then
        val openStartDateTime = bossStoreOpenTimeRepository.get(bossStoreId)
        assertThat(openStartDateTime).isNotNull
    }

    @Test
    fun `가게 오픈 갱신시 오픈 정보가 있는 경우 정보가 유지된채 만료시간만 연장된다`() {
        // given
        val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
        bossStoreOpenTimeRepository.set(bossStoreId, startDateTime)

        // when
        bossStoreOpenService.openBossStore(
            bossStoreId = bossStoreId,
            bossId = bossId,
            mapLocation = LocationValue.of(38.0, 127.0)
        )

        // then
        val openStartDateTime = bossStoreOpenTimeRepository.get(bossStoreId)
        assertThat(openStartDateTime).isEqualTo(startDateTime)
    }

    @Test
    fun `가게 오픈시 아직 위치 정보가 없는경우 위치 정보가 생성된다`() {
        // given
        val latitude = 37.0
        val longitude = 127.0

        // when
        bossStoreOpenService.openBossStore(
            bossStoreId = bossStoreId,
            bossId = bossId,
            mapLocation = LocationValue.of(latitude, longitude)
        )

        // then
        val bossStoreLocations = bossStoreRepository.findAll()
        assertAll({
            assertThat(bossStoreLocations).hasSize(1)
            assertBossStoreLocation(
                bossStoreLocation = bossStoreLocations[0],
                latitude = latitude,
                longitude = longitude
            )
        })
    }

    @Test
    fun `가게 오픈 갱신시 가게 위치가 변경되었을 경우 수정된다`() {
        // given
        val latitude = 37.3
        val longitude = 126.0

        // when
        bossStoreOpenService.openBossStore(bossStoreId, bossId, LocationValue.of(latitude, longitude))

        // then
        val bossStoreLocations = bossStoreRepository.findAll()
        assertAll({
            assertThat(bossStoreLocations).hasSize(1)
            assertBossStoreLocation(
                bossStoreLocation = bossStoreLocations[0],
                latitude = latitude,
                longitude = longitude
            )
        })
    }

    @Test
    fun `가게 오픈 정보 갱신시 존재하지 않는 가게인 경우 NotFound Exception`() {
        // when & then
        assertThatThrownBy {
            bossStoreOpenService.openBossStore(
                bossStoreId = "Not Found Boss StoreId",
                bossId = bossId,
                mapLocation = LocationValue.of(38.0, 128.0)
            )
        }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `가게 오픈 정보 갱신시 가게는 존재하되, 사장님이 아닌경우 NotFound Exception`() {
        // when & then
        assertThatThrownBy {
            bossStoreOpenService.openBossStore(
                bossStoreId = bossStoreId,
                bossId = "Not Owner BossId",
                mapLocation = LocationValue.of(38.0, 128.0)
            )
        }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `가게를 영업종료하면 오픈 정보가 레디스에서 삭제된다`() {
        // given
        val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
        bossStoreOpenTimeRepository.set(bossStoreId, startDateTime)

        // when
        bossStoreOpenService.closeBossStore(bossStoreId = bossStoreId, bossId = bossId)

        // then
        val openStartDateTime = bossStoreOpenTimeRepository.get(bossStoreId)
        assertThat(openStartDateTime).isNull()
    }

}


private fun assertBossStoreLocation(
    bossStoreLocation: BossStore,
    latitude: Double,
    longitude: Double,
) {
    assertThat(bossStoreLocation.location?.latitude).isEqualTo(latitude)
    assertThat(bossStoreLocation.location?.longitude).isEqualTo(longitude)
}
