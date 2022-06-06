package com.depromeet.threedollar.api.boss.controller.store

import java.time.LocalDateTime
import java.time.LocalTime
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.put
import com.depromeet.threedollar.api.boss.controller.SetupBossAccountControllerTest
import com.depromeet.threedollar.api.boss.service.store.dto.request.AppearanceDayRequest
import com.depromeet.threedollar.api.boss.service.store.dto.request.MenuRequest
import com.depromeet.threedollar.api.boss.service.store.dto.request.PatchBossStoreInfoRequest
import com.depromeet.threedollar.api.boss.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.bossservice.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreAppearanceDayResponse
import com.depromeet.threedollar.api.core.service.bossservice.store.dto.response.BossStoreMenuResponse
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.model.ContactsNumber
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreAppearanceDayCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreMenuCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreOpenType
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.store.BossStoreOpenTimeRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

internal class BossStoreControllerTest(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
) : SetupBossAccountControllerTest() {

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

    @DisplayName("PUT /v1/boss/store/{BOSS_STORE_ID}/open 200OK")
    @Nested
    inner class OpenBossStoreApiTest {

        @Test
        fun `가게의 영업 정보를 시작합니다`() {
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
                menus = listOf(BossStoreMenuCreator.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(BossStoreAppearanceDayCreator.create(
                    dayOfTheWeek = DayOfTheWeek.FRIDAY,
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(10, 0),
                    locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            // when & then
            mockMvc.put("/v1/boss/store/${bossStore.id}/open") {
                param("mapLatitude", "34.0")
                param("mapLongitude", "128.2")
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
        }

        @Test
        fun `이미 가게를 영업중일때 영업 정보를 갱신합니다`() {
            // given
            val category = BossStoreCategoryCreator.create("한식", 1)
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreCreator.create(
                bossId = bossId,
                name = "사장님 가게",
                location = BossStoreLocation.of(latitude = 38.0, longitude = 128.0),
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuCreator.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(BossStoreAppearanceDayCreator.create(
                    dayOfTheWeek = DayOfTheWeek.FRIDAY,
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(10, 0),
                    locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            bossStoreOpenTimeRepository.set(bossStore.id, LocalDateTime.of(2022, 2, 1, 0, 0))

            // when & then
            mockMvc.put("/v1/boss/store/${bossStore.id}/open") {
                param("mapLatitude", "34.0")
                param("mapLongitude", "128.2")
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
        }

    }

    @DisplayName("PUT /v1/boss/store/{BOSS_STORE_ID}/close 200OK")
    @Nested
    inner class CloseBossStoreApiTest {

        @Test
        fun `가게를 강제로 영업을 종료합니다`() {
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
                menus = listOf(BossStoreMenuCreator.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(BossStoreAppearanceDayCreator.create(
                    dayOfTheWeek = DayOfTheWeek.FRIDAY,
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(10, 0),
                    locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            // when & then
            mockMvc.put("/v1/boss/store/${bossStore.id}/close") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
        }

        @Test
        fun `가게를 영업중일때 가게를 강제 영업 종료합니다`() {
            // given
            val category = BossStoreCategoryCreator.create("한식", 1)
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreCreator.create(
                bossId = bossId,
                name = "사장님 가게",
                location = BossStoreLocation.of(latitude = 38.0, longitude = 128.0),
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuCreator.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(BossStoreAppearanceDayCreator.create(
                    dayOfTheWeek = DayOfTheWeek.FRIDAY,
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(10, 0),
                    locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            bossStoreOpenTimeRepository.set(bossStore.id, LocalDateTime.of(2022, 2, 1, 0, 0))

            // when & then
            mockMvc.put("/v1/boss/store/${bossStore.id}/open") {
                param("mapLatitude", "34.0")
                param("mapLongitude", "128.2")
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
        }

    }

    @DisplayName("GET /boss/v1/boss/store/me")
    @Nested
    inner class GetMyBossStoreApiTest {

        @Test
        fun `사장님 자신이 운영중인 가게를 조회합니다 오픈 정보가 레디스에 없으면 영업중이지 않은 가게로 표시된다`() {
            // given
            val category = BossStoreCategoryCreator.create("한식", 1)
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreCreator.create(
                bossId = bossId,
                name = "사장님 가게",
                location = BossStoreLocation.of(latitude = 38.0, longitude = 128.0),
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuCreator.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(BossStoreAppearanceDayCreator.create(
                    dayOfTheWeek = DayOfTheWeek.FRIDAY,
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(10, 0),
                    locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            // when & then
            mockMvc.get("/v1/boss/store/me") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
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

                jsonPath("$.data.openStatus.status") { value(BossStoreOpenType.CLOSED.name) }
                jsonPath("$.data.openStatus.openStartDateTime") { value(null) }
            }
        }

        @Test
        fun `사장님 자신이 운영중인 가게를 조회합니다 위치정보가_없는경우_location이_null로_반환된다`() {
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
                menus = listOf(BossStoreMenuCreator.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(BossStoreAppearanceDayCreator.create(
                    dayOfTheWeek = DayOfTheWeek.FRIDAY,
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(10, 0),
                    locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            // when & then
            mockMvc.get("/v1/boss/store/me") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.location") { value(null) }

                jsonPath("$.data.bossStoreId") { value(bossStore.id) }
                jsonPath("$.data.name") { value("사장님 가게") }

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

                jsonPath("$.data.openStatus.status") { value(BossStoreOpenType.CLOSED.name) }
                jsonPath("$.data.openStatus.openStartDateTime") { value(null) }
            }
        }

        @Test
        fun `사장님 자신이 운영중인 가게를 조회합니다 운영중인 가게가 없는경우 404 에러를 반환한다`() {
            // when & then
            mockMvc.get("/v1/boss/store/me") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isNotFound() }
            }
        }

    }

    @DisplayName("GET /boss/v1/boss/store/{BOSS_STORE_ID}")
    @Nested
    inner class GetBossStoreInfoApiTest {

        @Test
        fun `특정 가게를 조회합니다 오픈 정보가 레디스에 저장되어 있으면 영업중인 가게로 표시된다`() {
            // given
            val category = BossStoreCategoryCreator.create("한식", 1)
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
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
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

        @Test
        fun `특정 가게를 조회합니다 위치정보가 없는경우 위치정보가 null로 반환된다`() {
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
                menus = listOf(BossStoreMenuCreator.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(BossStoreAppearanceDayCreator.create(DayOfTheWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(10, 0), "강남역")),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            bossStoreOpenTimeRepository.set(bossStore.id, LocalDateTime.of(2022, 2, 1, 0, 0))

            // when & then
            mockMvc.get("/v1/boss/store/${bossStore.id}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }

                    jsonPath("$.data.location") { value(null) }

                    jsonPath("$.data.bossStoreId") { value(bossStore.id) }
                    jsonPath("$.data.name") { value("사장님 가게") }

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

        @Test
        fun `특정 가게를 조회할떄 해당하는 가게가 없는경우 404 에러가 발생한다`() {
            // given
            val bossStoreId = "notFoundBossStoreId"

            // when & then
            mockMvc.get("/v1/boss/store/${bossStoreId}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isNotFound() }
                    jsonPath("$.resultCode") { value(ErrorCode.NOT_FOUND_STORE.code) }
                    jsonPath("$.message") { value(ErrorCode.NOT_FOUND_STORE.message) }
                }
        }

    }

    @DisplayName("PUT /v1/boss/store/{BOSS_STORE_ID}")
    @Nested
    inner class UpdateBossStoreInfoApiTest {

        @Test
        fun `사장님 가게의 정보를 수정합니다 - PUT`() {
            // given
            val category = BossStoreCategoryCreator.create("중식", 1)
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreCreator.create(
                bossId = bossId,
                name = "사장님 가게",
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuCreator.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(BossStoreAppearanceDayCreator.create(
                    dayOfTheWeek = DayOfTheWeek.FRIDAY,
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(10, 0),
                    locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            val request = UpdateBossStoreInfoRequest(
                name = "변경 후 이름",
                imageUrl = "https://after.png",
                introduction = "변경 후 소개",
                contactsNumber = "010-1234-1234",
                snsUrl = "https://instagram.com",
                menus = listOf(
                    MenuRequest(name = "팥 붕어빵", price = 1000, imageUrl = "https://menu-bungeoppang.png")
                ),
                appearanceDays = setOf(
                    AppearanceDayRequest(
                        dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                        startTime = LocalTime.of(8, 0),
                        endTime = LocalTime.of(10, 0),
                        locationDescription = "강남역"
                    )
                ),
                categoriesIds = setOf(category.id)
            )

            // when & then
            mockMvc.put("/v1/boss/store/${bossStore.id}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }

                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
        }

    }

    @DisplayName("PATCH /v1/boss/store/{BOSS_STORE_ID}")
    @Nested
    inner class PatchBossStoreInfoApiTest {

        @Test
        fun `사장님의 가게를 수정합니다 - PATCH`() {
            // given
            val category = BossStoreCategoryCreator.create("중식", 1)
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreCreator.create(
                bossId = bossId,
                name = "사장님 가게",
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuCreator.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(BossStoreAppearanceDayCreator.create(
                    dayOfTheWeek = DayOfTheWeek.FRIDAY,
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(10, 0),
                    locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            val request = PatchBossStoreInfoRequest(
                name = "변경 후 이름",
                imageUrl = "https://after.png",
                introduction = "변경 후 소개",
                contactsNumber = "010-1234-1234",
                snsUrl = "https://instagram.com",
                menus = listOf(
                    MenuRequest(name = "팥 붕어빵", price = 1000, imageUrl = "https://menu-bungeoppang.png")
                ),
                appearanceDays = setOf(
                    AppearanceDayRequest(
                        dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                        startTime = LocalTime.of(9, 0),
                        endTime = LocalTime.of(11, 0),
                        locationDescription = "강남역"
                    )
                ),
                categoriesIds = setOf(category.id)
            )

            // when & then
            mockMvc.patch("/v1/boss/store/${bossStore.id}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }

                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
        }

    }

}
