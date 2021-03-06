package com.depromeet.threedollar.api.bossservice.controller.store

import com.depromeet.threedollar.api.bossservice.SetupBossAccountControllerTest
import com.depromeet.threedollar.api.core.service.service.bossservice.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.response.BossStoreAppearanceDayResponse
import com.depromeet.threedollar.api.core.service.service.bossservice.store.dto.response.BossStoreMenuResponse
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.model.ContactsNumber
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreAppearanceDayFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreLocation
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreMenuFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreOpenType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpenFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpenRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheRepository
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime
import java.time.LocalTime

internal class BossStoreRetrieveControllerTest(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenRepository: BossStoreOpenRepository,
    private val bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository,
) : SetupBossAccountControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreCategoryRepository.deleteAll()
        bossStoreRepository.deleteAll()
        bossStoreOpenRepository.deleteAll()
        bossStoreCategoryCacheRepository.cleanCache()
    }

    @DisplayName("GET /boss/v1/boss/store/me")
    @Nested
    inner class GetMyBossStoreApiTest {

        @Test
        fun `????????? ????????? ???????????? ????????? ???????????????`() {
            // given
            val category = BossStoreCategoryFixture.create(title = "??????", sequencePriority = 1, imageUrl = "https://icon1.png")
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "????????? ??????",
                location = BossStoreLocation.of(latitude = 38.0, longitude = 128.0),
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuFixture.create("?????????", 2000, "https://menu.png")),
                appearanceDays = setOf(
                    BossStoreAppearanceDayFixture.create(
                        dayOfTheWeek = DayOfTheWeek.FRIDAY,
                        startTime = LocalTime.of(8, 0),
                        endTime = LocalTime.of(10, 0),
                        locationDescription = "?????????")
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
                jsonPath("$.data.name") { value("????????? ??????") }

                jsonPath("$.data.location.latitude") { value(38.0) }
                jsonPath("$.data.location.longitude") { value(128.0) }

                jsonPath("$.data.imageUrl") { value(bossStore.imageUrl) }
                jsonPath("$.data.introduction") { value(bossStore.introduction) }
                jsonPath("$.data.snsUrl") { value(bossStore.snsUrl) }
                jsonPath("$.data.contactsNumber") { value("010-1234-1234") }

                jsonPath("$.data.menus", Matchers.hasSize<BossStoreMenuResponse>(1))
                jsonPath("$.data.menus[0].name") { value("?????????") }
                jsonPath("$.data.menus[0].price") { value(2000) }
                jsonPath("$.data.menus[0].imageUrl") { value("https://menu.png") }

                jsonPath("$.data.appearanceDays", Matchers.hasSize<BossStoreAppearanceDayResponse>(1))
                jsonPath("$.data.appearanceDays[0].dayOfTheWeek") { value(DayOfTheWeek.FRIDAY.name) }
                jsonPath("$.data.appearanceDays[0].openingHours.startTime") { value("08:00") }
                jsonPath("$.data.appearanceDays[0].openingHours.endTime") { value("10:00") }
                jsonPath("$.data.appearanceDays[0].locationDescription") { value("?????????") }

                jsonPath("$.data.categories", Matchers.hasSize<BossStoreCategoryResponse>(1))
                jsonPath("$.data.categories[0].categoryId") { value(category.id) }
                jsonPath("$.data.categories[0].name") { value("??????") }

                jsonPath("$.data.openStatus.status") { value(BossStoreOpenType.CLOSED.name) }
                jsonPath("$.data.openStatus.openStartDateTime") { value(null) }
            }
        }

        @Test
        fun `????????? ????????? ???????????? ????????? ??????????????? ?????? ????????? ???????????? ????????? ??????????????? ?????? ????????? ????????????`() {
            // given
            val category = BossStoreCategoryFixture.create()
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create(bossId = bossId)
            bossStoreRepository.save(bossStore)

            // when & then
            mockMvc.get("/v1/boss/store/me") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.openStatus.status") { value(BossStoreOpenType.CLOSED.name) }
                jsonPath("$.data.openStatus.openStartDateTime") { value(null) }
            }
        }

        @Test
        fun `????????? ????????? ???????????? ????????? ??????????????? ??????????????? ???????????? location??? null???_????????????`() {
            // given
            val category = BossStoreCategoryFixture.create()
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create(bossId = bossId)
            bossStoreRepository.save(bossStore)

            // when & then
            mockMvc.get("/v1/boss/store/me") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$.data.location") { value(null) }
            }
        }

        @Test
        fun `????????? ????????? ???????????? ????????? ??????????????? ???????????? ????????? ???????????? 404 ????????? ????????????`() {
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
        fun `?????? ????????? ???????????????`() {
            // given
            val category = BossStoreCategoryFixture.create(title = "??????", sequencePriority = 1, imageUrl = "https://icon1.png")
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create(
                bossId = "anotherBossId",
                name = "????????? ??????",
                location = BossStoreLocation.of(latitude = 38.0, longitude = 128.0),
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuFixture.create("?????????", 2000, "https://menu.png")),
                appearanceDays = setOf(BossStoreAppearanceDayFixture.create(DayOfTheWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(10, 0), "?????????")),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            val startDateTime = LocalDateTime.of(2022, 2, 1, 0, 0)
            val expiredAt = LocalDateTime.of(2999, 1, 1, 0, 0)

            val bossStoreOpen = BossStoreOpenFixture.create(
                bossStoreId = bossStore.id,
                openStartDateTime = startDateTime,
                expiredAt = expiredAt,
            )
            bossStoreOpenRepository.save(bossStoreOpen)

            // when & then
            mockMvc.get("/v1/boss/store/${bossStore.id}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }.andExpect {
                    status { isOk() }

                    jsonPath("$.data.bossStoreId") { value(bossStore.id) }
                    jsonPath("$.data.name") { value("????????? ??????") }

                    jsonPath("$.data.location.latitude") { value(38.0) }
                    jsonPath("$.data.location.longitude") { value(128.0) }

                    jsonPath("$.data.imageUrl") { value(bossStore.imageUrl) }
                    jsonPath("$.data.introduction") { value(bossStore.introduction) }
                    jsonPath("$.data.snsUrl") { value(bossStore.snsUrl) }
                    jsonPath("$.data.contactsNumber") { value("010-1234-1234") }

                    jsonPath("$.data.menus", Matchers.hasSize<BossStoreMenuResponse>(1))
                    jsonPath("$.data.menus[0].name") { value("?????????") }
                    jsonPath("$.data.menus[0].price") { value(2000) }
                    jsonPath("$.data.menus[0].imageUrl") { value("https://menu.png") }

                    jsonPath("$.data.appearanceDays", Matchers.hasSize<BossStoreAppearanceDayResponse>(1))
                    jsonPath("$.data.appearanceDays[0].dayOfTheWeek") { value(DayOfTheWeek.FRIDAY.name) }
                    jsonPath("$.data.appearanceDays[0].openingHours.startTime") { value("08:00") }
                    jsonPath("$.data.appearanceDays[0].openingHours.endTime") { value("10:00") }
                    jsonPath("$.data.appearanceDays[0].locationDescription") { value("?????????") }

                    jsonPath("$.data.categories", Matchers.hasSize<BossStoreCategoryResponse>(1))
                    jsonPath("$.data.categories[0].categoryId") { value(category.id) }
                    jsonPath("$.data.categories[0].name") { value("??????") }

                    jsonPath("$.data.openStatus.status") { value(BossStoreOpenType.OPEN.name) }
                    jsonPath("$.data.openStatus.openStartDateTime") { value("2022-02-01T00:00:00") }
                }
        }

        @Test
        fun `?????? ????????? ??????????????? ?????? ????????? ???????????? ???????????? ????????? ???????????? ????????? ????????????`() {
            // given
            val category = BossStoreCategoryFixture.create()
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create()
            bossStoreRepository.save(bossStore)

            val startDateTime = LocalDateTime.of(2022, 2, 1, 0, 0)
            val expiredAt = LocalDateTime.of(2999, 1, 1, 0, 0)

            val bossStoreOpen = BossStoreOpenFixture.create(
                bossStoreId = bossStore.id,
                openStartDateTime = startDateTime,
                expiredAt = expiredAt,
            )
            bossStoreOpenRepository.save(bossStoreOpen)

            // when & then
            mockMvc.get("/v1/boss/store/${bossStore.id}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }.andExpect {
                    status { isOk() }

                    jsonPath("$.data.openStatus.status") { value(BossStoreOpenType.OPEN.name) }
                    jsonPath("$.data.openStatus.openStartDateTime") { value("2022-02-01T00:00:00") }
                }
        }

        @Test
        fun `?????? ????????? ??????????????? ??????????????? ???????????? ??????????????? null??? ????????????`() {
            // given
            val category = BossStoreCategoryFixture.create()
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create()
            bossStoreRepository.save(bossStore)

            val startDateTime = LocalDateTime.of(2022, 2, 1, 0, 0)
            val expiredAt = LocalDateTime.of(2999, 1, 1, 0, 0)

            val bossStoreOpen = BossStoreOpenFixture.create(
                bossStoreId = bossStore.id,
                openStartDateTime = startDateTime,
                expiredAt = expiredAt,
            )
            bossStoreOpenRepository.save(bossStoreOpen)

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
                }
        }

        @Test
        fun `?????? ????????? ???????????? ???????????? ????????? ???????????? 404 ????????? ????????????`() {
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
                    jsonPath("$.resultCode") { value(ErrorCode.E404_NOT_EXISTS_STORE.code) }
                    jsonPath("$.message") { value(ErrorCode.E404_NOT_EXISTS_STORE.message) }
                }
        }

    }

}
