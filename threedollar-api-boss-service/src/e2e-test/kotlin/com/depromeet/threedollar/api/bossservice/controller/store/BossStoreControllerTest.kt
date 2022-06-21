package com.depromeet.threedollar.api.bossservice.controller.store

import java.time.LocalDateTime
import java.time.LocalTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import com.depromeet.threedollar.api.bossservice.SetupBossAccountControllerTest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.AppearanceDayRequest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.MenuRequest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.PatchBossStoreInfoRequest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.common.exception.type.ErrorCode
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
import com.depromeet.threedollar.domain.redis.domain.bossservice.store.BossStoreOpenTimeRepository

internal class BossStoreControllerTest(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenTimeRepository: BossStoreOpenTimeRepository,
) : SetupBossAccountControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreCategoryRepository.deleteAll()
        bossStoreRepository.deleteAll()
    }

    @DisplayName("POST /v1/boss/store/{BOSS_STORE_ID}/open 200OK")
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
                appearanceDays = setOf(
                    BossStoreAppearanceDayCreator.create(
                        dayOfTheWeek = DayOfTheWeek.FRIDAY,
                        startTime = LocalTime.of(8, 0),
                        endTime = LocalTime.of(10, 0),
                        locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            // when & then
            mockMvc.post("/v1/boss/store/${bossStore.id}/open") {
                param("mapLatitude", "34.0")
                param("mapLongitude", "128.2")
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.status") { value(BossStoreOpenType.OPEN.name) }
                jsonPath("$.data.openStartDateTime") { isNotEmpty() }
            }
        }

    }

    @DisplayName("PUT /v1/boss/store/{BOSS_STORE_ID}/renew")
    @Nested
    inner class PatchBossStoreApiTest {

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
                appearanceDays = setOf(
                    BossStoreAppearanceDayCreator.create(
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
            mockMvc.put("/v1/boss/store/${bossStore.id}/renew") {
                param("mapLatitude", "38.0")
                param("mapLongitude", "128.0")
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.status") { value(BossStoreOpenType.OPEN.name) }
                jsonPath("$.data.openStartDateTime") { isNotEmpty() }
            }
        }

        @Test
        fun `오픈 정보를 갱신할 수 있는 범위 밖인 경우 가게가 강제로 영업 종료된다`() {
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
                appearanceDays = setOf(
                    BossStoreAppearanceDayCreator.create(
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
            mockMvc.put("/v1/boss/store/${bossStore.id}/renew") {
                param("mapLatitude", "34.0")
                param("mapLongitude", "128.2")
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.status") { value(BossStoreOpenType.CLOSED.name) }
                jsonPath("$.data.openStartDateTime") { isEmpty() }
            }
        }

        @Test
        fun `오픈 중이지 않은 가게인 경우 Forbidden Exception이 발생한다`() {
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
                appearanceDays = setOf(
                    BossStoreAppearanceDayCreator.create(
                        dayOfTheWeek = DayOfTheWeek.FRIDAY,
                        startTime = LocalTime.of(8, 0),
                        endTime = LocalTime.of(10, 0),
                        locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            // when & then
            mockMvc.put("/v1/boss/store/${bossStore.id}/renew") {
                param("mapLatitude", "34.0")
                param("mapLongitude", "128.2")
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isForbidden() }
                jsonPath("$.resultCode") { value(ErrorCode.FORBIDDEN_NOT_OPEN_STORE.code) }
                jsonPath("$.message") { value(ErrorCode.FORBIDDEN_NOT_OPEN_STORE.message) }
            }
        }

    }

    @DisplayName("DELETE /v1/boss/store/{BOSS_STORE_ID}/close 200OK")
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
                appearanceDays = setOf(
                    BossStoreAppearanceDayCreator.create(
                        dayOfTheWeek = DayOfTheWeek.FRIDAY,
                        startTime = LocalTime.of(8, 0),
                        endTime = LocalTime.of(10, 0),
                        locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            // when & then
            mockMvc.delete("/v1/boss/store/${bossStore.id}/close") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.status") { value(BossStoreOpenType.CLOSED.name) }
                jsonPath("$.data.openStartDateTime") { isEmpty() }
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
                appearanceDays = setOf(
                    BossStoreAppearanceDayCreator.create(
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
            mockMvc.delete("/v1/boss/store/${bossStore.id}/close") {
                param("mapLatitude", "34.0")
                param("mapLongitude", "128.2")
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.status") { value(BossStoreOpenType.CLOSED.name) }
                jsonPath("$.data.openStartDateTime") { isEmpty() }
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
                appearanceDays = setOf(
                    BossStoreAppearanceDayCreator.create(
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
                appearanceDays = setOf(
                    BossStoreAppearanceDayCreator.create(
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
