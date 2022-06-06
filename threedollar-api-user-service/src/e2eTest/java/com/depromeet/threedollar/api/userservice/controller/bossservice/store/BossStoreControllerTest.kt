package com.depromeet.threedollar.api.userservice.controller.bossservice.store

import java.time.LocalDateTime
import java.time.LocalTime
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get
import com.depromeet.threedollar.api.core.service.bossservice.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreAppearanceDayResponse
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreMenuResponse
import com.depromeet.threedollar.api.userservice.controller.SetupUserControllerTest
import com.depromeet.threedollar.common.model.ContactsNumber
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryCreator
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreAppearanceDayCreator
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreCreator
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreMenuCreator
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreOpenType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.store.BossStoreOpenTimeRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

internal class BossStoreControllerTest(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
) : SetupUserControllerTest() {

    @MockkBean
    private lateinit var bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository

    @BeforeEach
    fun disableCacheCategories() {
        every { bossStoreCategoryCacheRepository.set(any()) } returns Unit
        every { bossStoreCategoryCacheRepository.getAll() } returns null
    }

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreCategoryRepository.deleteAll()
        bossStoreRepository.deleteAll()
    }

    @DisplayName("GET /boss/v1/boss/store/{BOSS_STORE_ID}")
    @Test
    fun `특정 사장님 가게를 조회할때, Redis에 영업 정보가 있으면 영업중인 가게로 표기된다`() {
        // given
        val category = BossStoreCategoryCreator.create(title = "한식", sequencePriority = 1)
        bossStoreCategoryRepository.save(category)

        val bossStore = BossStoreCreator.create(
            bossId = "anotherBossId",
            name = "사장님 가게",
            location = BossStoreLocation.of(latitude = 38.0, longitude = 128.0),
            imageUrl = "https://image.png",
            introduction = "introduction",
            snsUrl = "https://sns.com",
            contactsNumber = ContactsNumber.of("010-1234-1234"),
            menus = listOf(BossStoreMenuCreator.create("붕어빵", 2000, "https://menu.png")),
            appearanceDays = setOf(BossStoreAppearanceDayCreator.create(DayOfTheWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(10, 0), "강남역")),
            categoriesIds = setOf(category.id)
        )
        bossStoreRepository.save(bossStore)

        bossStoreOpenTimeRepository.set(bossStore.id, LocalDateTime.of(2022, 2, 1, 0, 0))

        // when & then
        mockMvc.get("/v1/boss/store/${bossStore.id}") {
            header(HttpHeaders.AUTHORIZATION, token)
        }
            .andDo {
                print()
            }.andExpect {
                status { isOk() }

                jsonPath("$.data.bossStoreId") { value(bossStore.id) }
                jsonPath("$.data.name") { value("사장님 가게") }

                jsonPath("$.data.location.latitude") { value(38.0) }
                jsonPath("$.data.location.longitude") { value(128.0) }

                jsonPath("$.data.imageUrl") { value(bossStore.imageUrl) }
                jsonPath("$.data.introduction") { value(bossStore.introduction) }
                jsonPath("$.data.snsUrl") { value(bossStore.snsUrl) }
                jsonPath("$.data.contactsNumber") { value("010-1234-1234") }

                jsonPath("$.data.menus", hasSize<BossStoreMenuResponse>(1))
                jsonPath("$.data.menus[0].name") { value("붕어빵") }
                jsonPath("$.data.menus[0].price") { value(2000) }
                jsonPath("$.data.menus[0].imageUrl") { value("https://menu.png") }

                jsonPath("$.data.appearanceDays", hasSize<BossStoreAppearanceDayResponse>(1))
                jsonPath("$.data.appearanceDays[0].dayOfTheWeek") { value(DayOfTheWeek.FRIDAY.name) }
                jsonPath("$.data.appearanceDays[0].openingHours.startTime") { value("08:00") }
                jsonPath("$.data.appearanceDays[0].openingHours.endTime") { value("10:00") }
                jsonPath("$.data.appearanceDays[0].locationDescription") { value("강남역") }

                jsonPath("$.data.categories", hasSize<BossStoreCategoryResponse>(1))
                jsonPath("$.data.categories[0].categoryId") { value(category.id) }
                jsonPath("$.data.categories[0].name") { value("한식") }

                jsonPath("$.data.openStatus.status") { value(BossStoreOpenType.OPEN.name) }
                jsonPath("$.data.openStatus.openStartDateTime") { value("2022-02-01T00:00:00") }
            }
    }

}