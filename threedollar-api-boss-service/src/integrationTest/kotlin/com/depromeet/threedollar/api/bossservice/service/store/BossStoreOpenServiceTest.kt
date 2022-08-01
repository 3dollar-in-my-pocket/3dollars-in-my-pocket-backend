package com.depromeet.threedollar.api.bossservice.service.store

import com.depromeet.threedollar.api.bossservice.SetupBossStoreIntegrationTest
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.LocationValue
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStore
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpenFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpenRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.LocalDateTime

internal class BossStoreOpenServiceTest(
    private val bossStoreOpenService: BossStoreOpenService,
    private val bossStoreOpenRepository: BossStoreOpenRepository,
) : SetupBossStoreIntegrationTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreRepository.deleteAll()
        bossStoreOpenRepository.deleteAll()
    }

    @Nested
    inner class OpenBossStoreTest {

        @Test
        fun `기존의 오픈 정보가 없는경우 현재시간으로 오픈정보가 레디스에 추가된다`() {
            // when
            bossStoreOpenService.openBossStore(
                bossStoreId = bossStoreId,
                bossId = bossId,
                mapLocation = LocationValue.of(38.0, 127.0)
            )

            // then
            val bossStoreOpens = bossStoreOpenRepository.findAll()
            assertAll({
                assertThat(bossStoreOpens).hasSize(1)
                bossStoreOpens[0].also {
                    assertThat(it.bossStoreId).isEqualTo(bossStoreId)
                    assertThat(it.openStartDateTime).isBetween(LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusMinutes(1))
                    assertThat(it.expiredAt).isBetween(LocalDateTime.now().minusMinutes(29), LocalDateTime.now().plusMinutes(30))
                }
            })
        }

        @Test
        fun `기존의 오픈 정보가 있는경우, 오픈 만료 시간이 연장된다`() {
            // given
            val latitude = 38.0
            val longitude = 127.0

            val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
            val expiredAt = LocalDateTime.of(2999, 1, 1, 0, 0)

            val bossStoreOpen = BossStoreOpenFixture.create(
                bossStoreId = bossStore.id,
                openStartDateTime = startDateTime,
                expiredAt = expiredAt,
            )
            bossStoreOpenRepository.save(bossStoreOpen)

            // when
            bossStoreOpenService.openBossStore(
                bossStoreId = bossStoreId,
                bossId = bossId,
                mapLocation = LocationValue.of(latitude, longitude)
            )

            // then
            val bossStoreOpens = bossStoreOpenRepository.findAll()
            assertAll({
                assertThat(bossStoreOpens).hasSize(1)
                bossStoreOpens[0].also {
                    assertThat(it.bossStoreId).isEqualTo(bossStoreId)
                    assertThat(it.expiredAt).isBetween(LocalDateTime.now().minusMinutes(29), LocalDateTime.now().plusMinutes(30))
                }
            })
        }

        @Test
        fun `기존의 오픈 정보가 없는경우 해당 위치로 가게 정보가 수정된다`() {
            // given
            val latitude = 38.0
            val longitude = 127.0

            // when
            bossStoreOpenService.openBossStore(
                bossStoreId = bossStoreId,
                bossId = bossId,
                mapLocation = LocationValue.of(latitude, longitude)
            )

            // then
            val bossStores = bossStoreRepository.findAll()
            assertAll({
                assertThat(bossStores).hasSize(1)
                assertBossStoreLocation(bossStore = bossStores[0], latitude = latitude, longitude = longitude)
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
    inner class RenewBossStoreOpenInfoStoreTest {

        @Test
        fun `가게 오픈 갱신시 오픈 정보가 있고, 영업 가능 거리 범위 안에 있는 경우, 영업 정보가 갱신된다`() {
            // given
            val latitude = 38.0
            val longitude = 126.0

            val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
            val expiredAt = LocalDateTime.of(2999, 1, 1, 0, 0)

            val bossStoreOpen = BossStoreOpenFixture.create(
                bossStoreId = bossStore.id,
                openStartDateTime = startDateTime,
                expiredAt = expiredAt,
            )
            bossStoreOpenRepository.save(bossStoreOpen)

            // when
            bossStoreOpenService.renewBossStoreOpenInfo(
                bossStoreId = bossStore.id,
                bossId = bossId,
                mapLocation = LocationValue.of(latitude, longitude)
            )

            // then
            val bossStoreOpens = bossStoreOpenRepository.findAll()
            assertAll({
                assertThat(bossStoreOpens).hasSize(1)
                bossStoreOpens[0].also {
                    assertThat(it.bossStoreId).isEqualTo(bossStore.id)
                    assertThat(it.openStartDateTime).isEqualTo(startDateTime)
                    assertThat(it.expiredAt).isBetween(LocalDateTime.now().minusMinutes(29), LocalDateTime.now().plusMinutes(30))
                }
            })
        }

        @Test
        fun `가게 오픈 갱신시 위치가 변경된 경우 가게의 위치 정보가 변경된다`() {
            // given
            val latitude = 38.0
            val longitude = 126.0

            val bossStoreOpen = BossStoreOpenFixture.create(
                bossStoreId = bossStore.id,
                openStartDateTime = LocalDateTime.of(2022, 1, 1, 0, 0),
                expiredAt = LocalDateTime.of(2999, 1, 1, 0, 0),
            )
            bossStoreOpenRepository.save(bossStoreOpen)

            // when
            bossStoreOpenService.renewBossStoreOpenInfo(
                bossStoreId = bossStore.id,
                bossId = bossId,
                mapLocation = LocationValue.of(latitude, longitude)
            )

            // then
            val bossStores = bossStoreRepository.findAll()
            assertAll({
                assertThat(bossStores).hasSize(1)
                assertThat(bossStores[0].location?.latitude).isEqualTo(latitude)
                assertThat(bossStores[0].location?.longitude).isEqualTo(longitude)
            })
        }

        @Test
        fun `가게 오픈 정보 갱신시, Forbidden Exception이 발생한다`() {
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
            val expiredAt = LocalDateTime.of(2999, 1, 1, 0, 0)

            val bossStoreOpen = BossStoreOpenFixture.create(
                bossStoreId = bossStoreId,
                openStartDateTime = startDateTime,
                expiredAt = expiredAt,
            )
            bossStoreOpenRepository.save(bossStoreOpen)

            // when
            bossStoreOpenService.closeBossStore(bossStoreId = bossStoreId, bossId = bossId)

            // then
            val bossStoreOpens = bossStoreOpenRepository.findAll()
            assertAll({
                assertThat(bossStoreOpens).isEmpty()
            })
        }

        @Test
        fun `가게를 영업종료할때, 오픈 정보가 없는 경우 오픈 정보가 삭제된 상태로 유지된다`() {
            // when
            bossStoreOpenService.closeBossStore(bossStoreId = bossStoreId, bossId = bossId)

            // then
            val bossStoreOpens = bossStoreOpenRepository.findAll()
            assertAll({
                assertThat(bossStoreOpens).isEmpty()
            })
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
