package com.depromeet.threedollar.api.boss.service.store

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.model.CoordinateValue
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocationCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.boss.domain.store.BossStoreOpenRedisKey
import com.depromeet.threedollar.domain.redis.boss.domain.store.BossStoreOpenRedisKeyCreator
import com.depromeet.threedollar.domain.redis.core.StringRedisRepository
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.geo.Point
import java.time.LocalDateTime

@SpringBootTest
internal class BossStoreOpenServiceTest(
    private val bossStoreOpenService: BossStoreOpenService,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreLocationRepository: BossStoreLocationRepository,
    private val bossStoreOpenInfoRepository: StringRedisRepository<BossStoreOpenRedisKey, LocalDateTime>
) : FunSpec({

    afterEach {
        withContext(Dispatchers.IO) {
            bossStoreRepository.deleteAll()
            bossStoreLocationRepository.deleteAll()
        }
    }

    context("가게의 오픈 정보를 갱신한다") {
        test("기존의 오픈 정보가 존재하지 않는 경우, 현재 시간으로 오픈 시간이 레디스에 추가된다") {
            // given
            val bossStore = BossStoreCreator.create(
                bossId = "bossId",
                name = "가게"
            )
            withContext(Dispatchers.IO) {
                bossStoreRepository.save(bossStore)
            }

            // when
            bossStoreOpenService.openBossStore(bossStore.id, bossStore.bossId, CoordinateValue.of(38.0, 127.0))

            // then
            val openStartDateTime = bossStoreOpenInfoRepository.get(BossStoreOpenRedisKeyCreator.create(bossStore.id))
            openStartDateTime shouldNotBe null
        }

        test("기존의 오픈 정보가 존재하는 경우, 기존 오픈 시간이 유지된채, 만료 시간만 연장된다") {
            // given
            val bossStore = BossStoreCreator.create(
                bossId = "bossId",
                name = "가게"
            )
            withContext(Dispatchers.IO) {
                bossStoreRepository.save(bossStore)
            }

            val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
            bossStoreOpenInfoRepository.set(BossStoreOpenRedisKeyCreator.create(bossStore.id), startDateTime)

            // when
            bossStoreOpenService.openBossStore(bossStore.id, bossStore.bossId, CoordinateValue.of(38.0, 127.0))

            // then
            val openStartDateTime = bossStoreOpenInfoRepository.get(BossStoreOpenRedisKeyCreator.create(bossStore.id))
            openStartDateTime shouldBe startDateTime
        }

        test("오픈시, 위치 정보가 아직 없는 경우 위치 정보가 추가된다") {
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
            bossStoreLocations shouldHaveSize 1
            bossStoreLocations[0].location shouldBe Point(longitude, latitude)
        }

        test("오픈시, 위치 정보가 변경된 경우 해당 위치로 수정된다") {
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
            bossStoreLocations shouldHaveSize 1
            bossStoreLocations[0].location shouldBe Point(longitude, latitude)
        }

        test("오픈시, 해당하는 가게가 존재하지 않는 경우 NotFound 에러가 발생한다") {
            // when & then
            shouldThrowExactly<NotFoundException> {
                bossStoreOpenService.openBossStore("Not Found Boss StoreId", "bossId", CoordinateValue.of(38.0, 128.0))
            }
        }

        test("오픈시, 해당 가게의 사장님이 아닌 경우 NotFound 에러가 발생한다") {
            // given
            val bossStore = BossStoreCreator.create(
                bossId = "bossId",
                name = "가게"
            )
            withContext(Dispatchers.IO) {
                bossStoreRepository.save(bossStore)
            }

            // when & then
            shouldThrowExactly<NotFoundException> {
                bossStoreOpenService.openBossStore(bossStore.id, "Not Owner BossId", CoordinateValue.of(38.0, 128.0))
            }
        }

        test("가게를 영업 종료하면, 오픈 정보가 레디스에서 삭제된다") {
            // given
            val bossStore = BossStoreCreator.create(
                bossId = "bossId",
                name = "가게"
            )
            withContext(Dispatchers.IO) {
                bossStoreRepository.save(bossStore)
            }
            val startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0)
            bossStoreOpenInfoRepository.set(BossStoreOpenRedisKeyCreator.create(bossStore.id), startDateTime)

            // when
            bossStoreOpenService.closeBossStore(bossStore.id, bossStore.bossId)

            // then
            val openStartDateTime = bossStoreOpenInfoRepository.get(BossStoreOpenRedisKeyCreator.create(bossStore.id))
            openStartDateTime shouldBe null
        }
    }

})
