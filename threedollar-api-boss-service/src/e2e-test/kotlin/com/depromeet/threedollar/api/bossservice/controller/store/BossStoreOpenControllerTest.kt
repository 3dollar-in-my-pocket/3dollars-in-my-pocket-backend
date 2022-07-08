package com.depromeet.threedollar.api.bossservice.controller.store

import java.time.LocalDateTime
import java.time.LocalTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import com.depromeet.threedollar.api.bossservice.SetupBossAccountControllerTest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.model.ContactsNumber
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreAppearanceDayFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreMenuFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpenFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpenRepository

internal class BossStoreOpenControllerTest(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenRepository: BossStoreOpenRepository,
) : SetupBossAccountControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreCategoryRepository.deleteAll()
        bossStoreRepository.deleteAll()
        bossStoreOpenRepository.deleteAll()
    }

    @DisplayName("POST /v1/boss/store/{BOSS_STORE_ID}/open 200OK")
    @Nested
    inner class OpenBossStoreApiTest {

        @Test
        fun `가게의 영업 정보를 시작합니다`() {
            // given
            val category = BossStoreCategoryFixture.create("한식", 1)
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "사장님 가게",
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuFixture.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(
                    BossStoreAppearanceDayFixture.create(
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
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
        }

    }

    @DisplayName("PUT /v1/boss/store/{BOSS_STORE_ID}/renew")
    @Nested
    inner class PatchBossStoreApiTest {

        @Test
        fun `이미 가게를 영업중일때 영업 정보를 갱신합니다`() {
            // given
            val category = BossStoreCategoryFixture.create("한식", 1)
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "사장님 가게",
                location = BossStoreLocation.of(latitude = 38.0, longitude = 128.0),
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuFixture.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(
                    BossStoreAppearanceDayFixture.create(
                        dayOfTheWeek = DayOfTheWeek.FRIDAY,
                        startTime = LocalTime.of(8, 0),
                        endTime = LocalTime.of(10, 0),
                        locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            val bossStoreOpen = BossStoreOpenFixture.create(
                bossStoreId = bossStore.id,
                openStartDateTime = LocalDateTime.of(2022, 1, 1, 0, 0),
                expiredAt = LocalDateTime.of(2999, 1, 1, 0, 0),
            )
            bossStoreOpenRepository.save(bossStoreOpen)

            // when & then
            mockMvc.put("/v1/boss/store/${bossStore.id}/renew") {
                param("mapLatitude", "38.0")
                param("mapLongitude", "128.0")
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
        }

        @Test
        fun `오픈 중이지 않은 가게인 경우 Forbidden Exception이 발생한다`() {
            // given
            val category = BossStoreCategoryFixture.create("한식", 1)
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "사장님 가게",
                location = BossStoreLocation.of(latitude = 38.0, longitude = 128.0),
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuFixture.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(
                    BossStoreAppearanceDayFixture.create(
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
            val category = BossStoreCategoryFixture.create("한식", 1)
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "사장님 가게",
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuFixture.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(
                    BossStoreAppearanceDayFixture.create(
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
                jsonPath("$.data") { value(ApiResponse.OK.data) }
            }
        }

        @Test
        fun `가게를 영업중일때 가게를 강제 영업 종료합니다`() {
            // given
            val category = BossStoreCategoryFixture.create("한식", 1)
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "사장님 가게",
                location = BossStoreLocation.of(latitude = 38.0, longitude = 128.0),
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuFixture.create("붕어빵", 2000, "https://menu.png")),
                appearanceDays = setOf(
                    BossStoreAppearanceDayFixture.create(
                        dayOfTheWeek = DayOfTheWeek.FRIDAY,
                        startTime = LocalTime.of(8, 0),
                        endTime = LocalTime.of(10, 0),
                        locationDescription = "강남역")
                ),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            val bossStoreOpen = BossStoreOpenFixture.create(
                bossStoreId = bossStore.id,
                openStartDateTime = LocalDateTime.of(2022, 1, 1, 0, 0),
                expiredAt = LocalDateTime.of(2999, 1, 1, 0, 0),
            )
            bossStoreOpenRepository.save(bossStoreOpen)

            // when & then
            mockMvc.delete("/v1/boss/store/${bossStore.id}/close") {
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

}
