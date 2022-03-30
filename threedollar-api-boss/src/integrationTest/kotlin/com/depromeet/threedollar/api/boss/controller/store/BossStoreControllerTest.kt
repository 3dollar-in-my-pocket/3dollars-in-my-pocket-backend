package com.depromeet.threedollar.api.boss.controller.store

import com.depromeet.threedollar.api.boss.controller.SetupBossControllerTest
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreAppearanceDayResponse
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreMenuResponse
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.*
import com.depromeet.threedollar.domain.mongo.common.domain.ContactsNumber
import com.depromeet.threedollar.domain.mongo.common.domain.TimeInterval
import com.depromeet.threedollar.domain.redis.boss.domain.store.BossStoreOpenRedisRepository
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime
import java.time.LocalTime

internal class BossStoreControllerTest(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreLocationRepository: BossStoreLocationRepository,
    private val bossStoreOpenRedisRepository: BossStoreOpenRedisRepository
) : SetupBossControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreCategoryRepository.deleteAll()
        bossStoreRepository.deleteAll()
        bossStoreLocationRepository.deleteAll()
    }

    @DisplayName("GET /boss/v1/boss/store/my-store")
    @Test
    fun `사장님 자신이 운영중인 가게를 조회합니다 오픈 정보가 레디스에 없으면 영업중이지 않은 가게로 표시된다`() {
        // given
        val category = BossStoreCategoryCreator.create("한식", 1)
        bossStoreCategoryRepository.save(category)

        val bossStore = BossStoreCreator.create(
            bossId = bossId,
            name = "사장님 가게",
            imageUrl = "https://image.png",
            introduction = "introduction",
            snsUrl = "https://sns.com",
            contactsNumber = ContactsNumber.of("010-1234-1234"),
            menus = listOf(BossStoreMenu("붕어빵", 2000, "https://menu.png", "붕어빵")),
            appearanceDays = setOf(BossStoreAppearanceDay(DayOfTheWeek.FRIDAY, TimeInterval(LocalTime.of(8, 0), LocalTime.of(10, 0)), "강남역")),
            categoriesIds = setOf(category.id)
        )
        bossStoreRepository.save(bossStore)

        bossStoreLocationRepository.save(BossStoreLocationCreator.create(
            bossStoreId = bossStore.id,
            latitude = 38.0,
            longitude = 128.0
        ))

        // when & then
        mockMvc.get("/v1/boss/store/my-store") {
            header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
            content {
                jsonPath("$.data.bossStoreId") { value(bossStore.id) }
                jsonPath("$.data.name") { value("사장님 가게") }

                jsonPath("$.data.location.latitude") { value(38.0) }
                jsonPath("$.data.location.longitude") { value(128.0) }

                jsonPath("$.data.imageUrl") { value(bossStore.imageUrl) }
                jsonPath("$.data.introduction") { value(bossStore.introduction) }
                jsonPath("$.data.snsUrl") { value(bossStore.snsUrl) }
                jsonPath("$.data.contactsNumber") { value("010-1234-1234") }

                jsonPath("$.data.menus") { hasSize<BossStoreMenuResponse>(1) }
                jsonPath("$.data.menus[0].name") { value("붕어빵") }
                jsonPath("$.data.menus[0].price") { value(2000) }
                jsonPath("$.data.menus[0].imageUrl") { value("https://menu.png") }
                jsonPath("$.data.menus[0].groupName") { value("붕어빵") }

                jsonPath("$.data.appearanceDays") { hasSize<BossStoreAppearanceDayResponse>(1) }
                jsonPath("$.data.appearanceDays[0].dayOfTheWeek") { value(DayOfTheWeek.FRIDAY.name) }
                jsonPath("$.data.appearanceDays[0].openingHours.startTime") { value("08:00") }
                jsonPath("$.data.appearanceDays[0].openingHours.endTime") { value("10:00") }
                jsonPath("$.data.appearanceDays[0].locationDescription") { value("강남역") }

                jsonPath("$.data.categories") { hasSize<BossStoreCategoryResponse>(1) }
                jsonPath("$.data.categories[0].categoryId") { value(category.id) }
                jsonPath("$.data.categories[0].name") { value("한식") }

                jsonPath("$.data.openStatus.status") { value(BossStoreOpenType.CLOSED.name) }
                jsonPath("$.data.openStatus.openStartDateTime") { value(null) }
            }
        }
    }

    @DisplayName("GET /boss/v1/boss/store/{BOSS_STORE_ID}")
    @Test
    fun `특정 가게를 조회합니다 오픈 정보가 레디스에 저장되어 있으면 영업중인 가게로 표시된다`() {
        // given
        val category = BossStoreCategoryCreator.create("한식", 1)
        bossStoreCategoryRepository.save(category)

        val bossStore = BossStoreCreator.create(
            bossId = "anotherBossId",
            name = "사장님 가게",
            imageUrl = "https://image.png",
            introduction = "introduction",
            snsUrl = "https://sns.com",
            contactsNumber = ContactsNumber.of("010-1234-1234"),
            menus = listOf(BossStoreMenu("붕어빵", 2000, "https://menu.png", "붕어빵")),
            appearanceDays = setOf(BossStoreAppearanceDay(DayOfTheWeek.FRIDAY, TimeInterval(LocalTime.of(8, 0), LocalTime.of(10, 0)), "강남역")),
            categoriesIds = setOf(category.id)
        )
        bossStoreRepository.save(bossStore)

        bossStoreOpenRedisRepository.set(bossStore.id, LocalDateTime.of(2022, 2, 1, 0, 0))

        bossStoreLocationRepository.save(BossStoreLocationCreator.create(
            bossStoreId = bossStore.id,
            latitude = 38.0,
            longitude = 128.0
        ))

        // when & then
        mockMvc.get("/v1/boss/store/${bossStore.id}")
            .andDo {
                print()
            }.andExpect {
                status { isOk() }
                content {
                    jsonPath("$.data.bossStoreId") { value(bossStore.id) }
                    jsonPath("$.data.name") { value("사장님 가게") }

                    jsonPath("$.data.location.latitude") { value(38.0) }
                    jsonPath("$.data.location.longitude") { value(128.0) }

                    jsonPath("$.data.imageUrl") { value(bossStore.imageUrl) }
                    jsonPath("$.data.introduction") { value(bossStore.introduction) }
                    jsonPath("$.data.snsUrl") { value(bossStore.snsUrl) }
                    jsonPath("$.data.contactsNumber") { value("010-1234-1234") }

                    jsonPath("$.data.menus") { hasSize<BossStoreMenuResponse>(1) }
                    jsonPath("$.data.menus[0].name") { value("붕어빵") }
                    jsonPath("$.data.menus[0].price") { value(2000) }
                    jsonPath("$.data.menus[0].imageUrl") { value("https://menu.png") }
                    jsonPath("$.data.menus[0].groupName") { value("붕어빵") }

                    jsonPath("$.data.appearanceDays") { hasSize<BossStoreAppearanceDayResponse>(1) }
                    jsonPath("$.data.appearanceDays[0].dayOfTheWeek") { value(DayOfTheWeek.FRIDAY.name) }
                    jsonPath("$.data.appearanceDays[0].openingHours.startTime") { value("08:00") }
                    jsonPath("$.data.appearanceDays[0].openingHours.endTime") { value("10:00") }
                    jsonPath("$.data.appearanceDays[0].locationDescription") { value("강남역") }

                    jsonPath("$.data.categories") { hasSize<BossStoreCategoryResponse>(1) }
                    jsonPath("$.data.categories[0].categoryId") { value(category.id) }
                    jsonPath("$.data.categories[0].name") { value("한식") }

                    jsonPath("$.data.openStatus.status") { value(BossStoreOpenType.OPEN.name) }
                    jsonPath("$.data.openStatus.openStartDateTime") { value("2022-02-01T00:00:00") }
                }
            }
    }

}
