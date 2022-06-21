package com.depromeet.threedollar.api.bossservice.service.store

import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import com.depromeet.threedollar.api.bossservice.SetupBossStoreIntegrationTest
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreCreator
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreOpenType
import com.depromeet.threedollar.domain.redis.domain.bossservice.store.BossStoreOpenTimeRepository

internal class BossStoreOpenServiceTest(
    private val bossStoreOpenService: BossStoreOpenService,
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
) : SetupBossStoreIntegrationTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreRepository.deleteAll()
    }

    @Nested
    inner class OpenBossStoreTest {

        @Test
        fun `기존의 오픈 정보가 없는경우 현재시간으로 오픈정보가 레디스에 추가된다`() {
            // when
            val response = bossStoreOpenService.openBossStore(
                bossStoreId = bossStoreId,
                bossId = bossId,
                mapLocation = LocationValue.of(38.0, 127.0)
            )

            // then
            val openStartDateTime = bossStoreOpenTimeRepository.get(bossStoreId)
            assertAll({
                assertThat(openStartDateTime).isNotNull

                assertThat(response.status).isEqualTo(BossStoreOpenType.OPEN)
                assertThat(response.openStartDateTime).isNotNull
            })
        }

        @Test
        fun `기존의 오픈 정보가 없는경우 해당 위치로 가게 정보가 수정된다`() {
            // given
            val latitude = 38.0
            val longitude = 127.0

            // when
            val response = bossStoreOpenService.openBossStore(
                bossStoreId = bossStoreId,
                bossId = bossId,
                mapLocation = LocationValue.of(latitude, longitude)
            )

            // then
            val bossStores = bossStoreRepository.findAll()
            assertAll({
                assertThat(bossStores).hasSize(1)
                assertBossStoreLocation(bossStore = bossStores[0], latitude = latitude, longitude = longitude)

                assertThat(response.status).isEqualTo(BossStoreOpenType.OPEN)
                assertThat(response.openStartDateTime).isNotNull
            })
        }

        @Test
        fun `가게 오픈시, 존재하지 않는 가게인 경우 NotFound Exception`() {
            // when & then
            assertThatThrownBy {
                bossStoreOpenService.openBossStore(
                    bossStoreId = "Not Found Boss StoreId",
                    bossId = bossId,
                    mapLocation = LocationValue.of(38.0, 128.0),
                )
            }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `가게 오픈시, 가게는 존재하되, 사장님이 아닌경우 NotFound Exception`() {
            // when & then
            assertThatThrownBy {
                bossStoreOpenService.openBossStore(
                    bossStoreId = bossStoreId,
                    bossId = "Not Owner BossId",
                    mapLocation = LocationValue.of(38.0, 128.0),
                )
            }.isInstanceOf(NotFoundException::class.java)
        }

    }

    @Nested
    inner class PatchBossStoreTest {

        @Test
        fun `가게 오픈 갱신시 오픈 정보가 있고, 영업 가능 거리 범위 안에 있는 경우, 영업 정보가 갱신된다`() {
            // given
            val latitude = 38.0
            val longitude = 126.0

            val bossStore = BossStoreCreator.create(
                bossId = bossId,
                name = "사장님 가게",
                location = BossStoreLocation.of(latitude = latitude, longitude = longitude)
            )
            bossStoreRepository.save(bossStore)

            val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
            bossStoreOpenTimeRepository.set(bossStore.id, startDateTime)

            // when
            val response = bossStoreOpenService.renewBossStoreOpenInfo(
                bossStoreId = bossStore.id,
                bossId = bossId,
                mapLocation = LocationValue.of(latitude, longitude)
            )

            // then
            val openStartDateTime = bossStoreOpenTimeRepository.get(bossStore.id)
            assertAll({
                assertThat(openStartDateTime).isEqualTo(startDateTime)
                assertThat(response.status).isEqualTo(BossStoreOpenType.OPEN)
                assertThat(response.openStartDateTime).isNotNull
            })
        }

        @Test
        fun `가게 오픈 갱신시 오픈 정보가 있지만, 영업 가능 거리 밖에 있는 경우 강제로 영업이 종료된다`() {
            // given
            val latitude = 38.0
            val longitude = 126.0

            val bossStore = BossStoreCreator.create(
                bossId = bossId,
                name = "사장님 가게",
                location = BossStoreLocation.of(latitude = latitude, longitude = longitude)
            )
            bossStoreRepository.save(bossStore)

            val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
            bossStoreOpenTimeRepository.set(bossStore.id, startDateTime)

            // when
            val response = bossStoreOpenService.renewBossStoreOpenInfo(
                bossStoreId = bossStore.id,
                bossId = bossId,
                mapLocation = LocationValue.of(36.0, 125.0)
            )

            // then
            val openStartDateTime = bossStoreOpenTimeRepository.get(bossStore.id)
            assertAll({
                assertThat(openStartDateTime).isNull()
                assertThat(response.status).isEqualTo(BossStoreOpenType.CLOSED)
                assertThat(response.openStartDateTime).isNull()
            })
        }

        @Test
        fun `가게 오픈 정보 갱신시, 기존의 오픈 정보가 없는경우 Forbidden Exception이 발생한다`() {
            // when & then
            assertThatThrownBy {
                bossStoreOpenService.renewBossStoreOpenInfo(
                    bossStoreId = bossStoreId,
                    bossId = bossId,
                    mapLocation = LocationValue.of(38.0, 127.0)
                )
            }.isInstanceOf(ForbiddenException::class.java)
        }

        @Test
        fun `가게 오픈 정보 갱신시 존재하지 않는 가게인 경우 NotFound Exception`() {
            // when & then
            assertThatThrownBy {
                bossStoreOpenService.renewBossStoreOpenInfo(
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
                bossStoreOpenService.renewBossStoreOpenInfo(
                    bossStoreId = bossStoreId,
                    bossId = "Not Owner BossId",
                    mapLocation = LocationValue.of(38.0, 128.0)
                )
            }.isInstanceOf(NotFoundException::class.java)
        }

    }

    @Nested
    inner class CloseBossStoreTest {

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

        @Test
        fun `가게 영업 종료시 존재하지 않는 가게인 경우 NotFound Exception`() {
            // when & then
            assertThatThrownBy {
                bossStoreOpenService.closeBossStore(
                    bossStoreId = "Not Found Boss StoreId",
                    bossId = bossId,
                )
            }.isInstanceOf(NotFoundException::class.java)
        }

        @Test
        fun `가게 영업 종료시 가게는 존재하되, 사장님이 아닌경우 NotFound Exception`() {
            // when & then
            assertThatThrownBy {
                bossStoreOpenService.closeBossStore(
                    bossStoreId = bossStoreId,
                    bossId = "Not Owner BossId",
                )
            }.isInstanceOf(NotFoundException::class.java)
        }

    }

}


private fun assertBossStoreLocation(
    bossStore: BossStore,
    latitude: Double,
    longitude: Double,
) {
    assertThat(bossStore.location?.latitude).isEqualTo(latitude)
    assertThat(bossStore.location?.longitude).isEqualTo(longitude)
}
