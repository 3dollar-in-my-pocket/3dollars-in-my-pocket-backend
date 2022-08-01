package com.depromeet.threedollar.api.bossservice.controller.store

import com.depromeet.threedollar.api.bossservice.SetupBossStoreControllerTest
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpenFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.storeopen.BossStoreOpenRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.LocalDateTime

internal class BossStoreOpenControllerTest(
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreOpenRepository: BossStoreOpenRepository,
) : SetupBossStoreControllerTest() {

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
        fun `가게의 영업 정보를 갱신합니다`() {
            // given
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

    }

    @DisplayName("DELETE /v1/boss/store/{BOSS_STORE_ID}/close 200OK")
    @Nested
    inner class CloseBossStoreApiTest {

        @Test
        fun `가게를 영업중일때 가게를 강제 영업 종료합니다`() {
            // given
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
