package com.depromeet.threedollar.api.bossservice.controller.store

import com.depromeet.threedollar.api.bossservice.SetupBossAccountControllerTest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.AppearanceDayRequest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.MenuRequest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.PatchBossStoreInfoRequest
import com.depromeet.threedollar.api.bossservice.service.store.dto.request.UpdateBossStoreInfoRequest
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.common.model.ContactsNumber
import com.depromeet.threedollar.common.type.DayOfTheWeek
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreAppearanceDayFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreMenuFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.put
import java.time.LocalTime

internal class BossStoreControllerTest(
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
) : SetupBossAccountControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        bossStoreCategoryRepository.deleteAll()
        bossStoreRepository.deleteAll()
    }

    @DisplayName("PUT /v1/boss/store/{BOSS_STORE_ID}")
    @Nested
    inner class UpdateBossStoreInfoApiTest {

        @Test
        fun `????????? ????????? ????????? ??????????????? - PUT`() {
            // given
            val category = BossStoreCategoryFixture.create()
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "????????? ??????",
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuFixture.create()),
                appearanceDays = setOf(BossStoreAppearanceDayFixture.create(dayOfTheWeek = DayOfTheWeek.FRIDAY)),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            val request = UpdateBossStoreInfoRequest(
                name = "?????? ??? ??????",
                imageUrl = "https://after.png",
                introduction = "?????? ??? ??????",
                contactsNumber = "010-1234-1234",
                snsUrl = "https://instagram.com",
                menus = listOf(
                    MenuRequest(name = "??? ?????????", price = 1000, imageUrl = "https://menu-bungeoppang.png")
                ),
                appearanceDays = setOf(
                    AppearanceDayRequest(
                        dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                        startTime = LocalTime.of(8, 0),
                        endTime = LocalTime.of(10, 0),
                        locationDescription = "?????????"
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
        fun `???????????? ????????? ??????????????? - PATCH`() {
            // given
            val category = BossStoreCategoryFixture.create()
            bossStoreCategoryRepository.save(category)

            val bossStore = BossStoreFixture.create(
                bossId = bossId,
                name = "????????? ??????",
                imageUrl = "https://image.png",
                introduction = "introduction",
                snsUrl = "https://sns.com",
                contactsNumber = ContactsNumber.of("010-1234-1234"),
                menus = listOf(BossStoreMenuFixture.create()),
                appearanceDays = setOf(BossStoreAppearanceDayFixture.create(dayOfTheWeek = DayOfTheWeek.SUNDAY)),
                categoriesIds = setOf(category.id)
            )
            bossStoreRepository.save(bossStore)

            val request = PatchBossStoreInfoRequest(
                name = "?????? ??? ??????",
                imageUrl = "https://after.png",
                introduction = "?????? ??? ??????",
                contactsNumber = "010-1234-1234",
                snsUrl = "https://instagram.com",
                menus = listOf(
                    MenuRequest(name = "??? ?????????", price = 1000, imageUrl = "https://menu-bungeoppang.png")
                ),
                appearanceDays = setOf(
                    AppearanceDayRequest(
                        dayOfTheWeek = DayOfTheWeek.WEDNESDAY,
                        startTime = LocalTime.of(9, 0),
                        endTime = LocalTime.of(11, 0),
                        locationDescription = "?????????"
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
