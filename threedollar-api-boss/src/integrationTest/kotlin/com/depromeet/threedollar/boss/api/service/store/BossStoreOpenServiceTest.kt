package com.depromeet.threedollar.boss.api.service.store

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.CoordinateValue
import com.depromeet.threedollar.document.boss.document.store.*
import com.depromeet.threedollar.cache.boss.domain.store.BossStoreOpenInfoCreator
import com.depromeet.threedollar.cache.redis.boss.store.BossStoreOpenInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.geo.Point
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class BossStoreOpenServiceTest(
    private val bossStoreOpenService: BossStoreOpenService,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreLocationRepository: BossStoreLocationRepository,
    private val bossStoreOpenInfoRepository: BossStoreOpenInfoRepository
) {

    @AfterEach
    fun cleanUp() {
        bossStoreRepository.deleteAll()
        bossStoreOpenInfoRepository.deleteAll()
        bossStoreLocationRepository.deleteAll()
    }

    @Test
    fun `기존의 오픈 정보가 없는경우 현재시간으로 오픈정보가 레디스에 추가된다`() {
        // given
        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "가게"
        )
        bossStoreRepository.save(bossStore)

        // when
        bossStoreOpenService.openBossStore(bossStore.id, bossStore.bossId, CoordinateValue.of(38.0, 127.0))

        // then
        val bossStoreOpenInfos = bossStoreOpenInfoRepository.findAll()
        assertThat(bossStoreOpenInfos).hasSize(1)
        assertThat(bossStoreOpenInfos[0].bossStoreId).isEqualTo(bossStore.id)
    }

    @Test
    fun `가게 오픈 갱신시 오픈 정보가 있는 경우 정보가 유지된채 만료시간만 연장된다`() {
        // given
        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "가게"
        )
        bossStoreRepository.save(bossStore)

        val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
        bossStoreOpenInfoRepository.save(BossStoreOpenInfoCreator.create(bossStore.id, startDateTime))

        // when
        bossStoreOpenService.openBossStore(bossStore.id, bossStore.bossId, CoordinateValue.of(38.0, 127.0))

        // then
        val bossStoreOpenInfos = bossStoreOpenInfoRepository.findAll()
        assertAll({
            assertThat(bossStoreOpenInfos).hasSize(1)
            assertThat(bossStoreOpenInfos[0].bossStoreId).isEqualTo(bossStore.id)
            assertThat(bossStoreOpenInfos[0].openStartDateTime).isEqualTo(startDateTime)
        })
    }

    @Test
    fun `가게 오픈시 아직 위치 정보가 없는경우 위치 정보가 생성된다`() {
        // given
        val latitude = 37.3
        val longitude = 129.2

        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "가게"
        )
        bossStoreRepository.save(bossStore)

        // when
        bossStoreOpenService.openBossStore(bossStore.id, bossStore.bossId, CoordinateValue.of(latitude, longitude))

        // then
        val bossStoreLocations = bossStoreLocationRepository.findAll()
        assertAll({
            assertThat(bossStoreLocations).hasSize(1)
            assertThat(bossStoreLocations[0].location).isEqualTo(Point(longitude, latitude))
        })
    }

    @Test
    fun `가게 오픈 갱신시 가게 위치가 변경되었을 경우 수정된다`() {
        // given
        val latitude = 37.3
        val longitude = 129.2

        val bossStore = bossStoreRepository.save(
            BossStoreCreator.create(
                bossId = "bossId",
                name = "가게"
            )
        )

        bossStoreLocationRepository.save(
            BossStoreLocationCreator.create(
                bossStoreId = bossStore.id,
                latitude = 36.0,
                longitude = 128.0
            )
        )

        // when
        bossStoreOpenService.openBossStore(bossStore.id, bossStore.bossId, CoordinateValue.of(latitude, longitude))

        // then
        val bossStoreLocations = bossStoreLocationRepository.findAll()
        assertAll({
            assertThat(bossStoreLocations).hasSize(1)
            assertThat(bossStoreLocations[0].location).isEqualTo(Point(longitude, latitude))
        })
    }

    @Test
    fun `가게 오픈 정보 갱신시 존재하지 않는 가게인 경우 NotFound Exception`() {
        // when & then
        assertThatThrownBy {
            bossStoreOpenService.openBossStore("Not Found Boss StoreId", "bossId", CoordinateValue.of(38.0, 128.0))
        }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `가게 오픈 정보 갱신시 가게는 존재하되, 사장님이 아닌경우 NotFound Exception`() {
        // given
        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "가게"
        )
        bossStoreRepository.save(bossStore)

        // when & then
        assertThatThrownBy {
            bossStoreOpenService.openBossStore(bossStore.id, "Not Owner BossId", CoordinateValue.of(38.0, 128.0))
        }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun `가게를 영업종료하면 오픈 정보가 레디스에서 삭제된다`() {
        // given
        val bossStore = BossStoreCreator.create(
            bossId = "bossId",
            name = "가게"
        )
        bossStoreRepository.save(bossStore)
        val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
        bossStoreOpenInfoRepository.save(BossStoreOpenInfoCreator.create(bossStore.id, startDateTime))

        // when
        bossStoreOpenService.closeBossStore(bossStore.id, bossStore.bossId)

        // then
        val bossStoreOpenInfos = bossStoreOpenInfoRepository.findAll()
        assertThat(bossStoreOpenInfos).isEmpty()
    }

}
