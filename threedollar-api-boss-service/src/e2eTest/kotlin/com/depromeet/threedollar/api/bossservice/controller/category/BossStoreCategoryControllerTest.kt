package com.depromeet.threedollar.api.bossservice.controller.category

import com.depromeet.threedollar.api.bossservice.SetupControllerTest
import com.depromeet.threedollar.api.core.service.service.bossservice.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryFixture
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheModel
import com.depromeet.threedollar.domain.redis.domain.bossservice.category.BossStoreCategoryCacheRepository
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get

internal class BossStoreCategoryControllerTest(
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository,
) : SetupControllerTest() {

    @AfterEach
    fun cleanUp() {
        bossStoreCategoryRepository.deleteAll()
        bossStoreCategoryCacheRepository.cleanCache()
    }

    @DisplayName("GET /boss/v1/boss/store/categories")
    @Nested
    inner class RetrieveBossStoreCategoriesApiTest {

        @Test
        fun `카테고리 목록이 캐시에 없는 경우, MongoDB에서 가져온 데이터를 반환한다`() {
            // given
            val category1 = BossStoreCategoryFixture.create(title = "한식", sequencePriority = 1, imageUrl = "https://icon11.png")
            val category2 = BossStoreCategoryFixture.create(title = "중식", sequencePriority = 2, imageUrl = "https://icon12.png")
            bossStoreCategoryRepository.saveAll(listOf(category1, category2))

            // when & then
            mockMvc.get("/v1/boss/store/categories")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data", hasSize<BossStoreCategoryResponse>(2))

                    jsonPath("$.data[0].categoryId") { value(category1.id) }
                    jsonPath("$.data[0].name") { value(category1.name) }

                    jsonPath("$.data[1].categoryId") { value(category2.id) }
                    jsonPath("$.data[1].name") { value(category2.name) }
                }
        }

        @Test
        fun `카테고리 목록이 캐시에 있을 경우, 캐시에서 가져온 데이터로 반환한다 - EMPTY List`() {
            // given
            bossStoreCategoryCacheRepository.setCache(emptyList())

            val category1 = BossStoreCategoryFixture.create(title = "한식", sequencePriority = 1, imageUrl = "https://icon100.png")
            val category2 = BossStoreCategoryFixture.create(title = "중식", sequencePriority = 2, imageUrl = "https://icon101.png")
            bossStoreCategoryRepository.saveAll(listOf(category1, category2))

            // when & then
            mockMvc.get("/v1/boss/store/categories")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data", hasSize<BossStoreCategoryResponse>(0))
                }
        }

        @Test
        fun `카테고리 목록이 캐시에 있을 경우, 캐시에서 가져온 데이터로 반환한다 - NOT EMPTY LIST`() {
            // given
            val category1 = BossStoreCategoryCacheModel.of(categoryId = "categoryId1", name = "한식", imageUrl = "https://icon01.png")
            val category2 = BossStoreCategoryCacheModel.of(categoryId = "categoryId1", name = "양식", imageUrl = "https://icon02.png")
            bossStoreCategoryCacheRepository.setCache(listOf(category1, category2))

            // when & then
            mockMvc.get("/v1/boss/store/categories")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data", hasSize<BossStoreCategoryResponse>(2))

                    jsonPath("$.data[0].categoryId") { value(category1.categoryId) }
                    jsonPath("$.data[0].name") { value(category1.name) }

                    jsonPath("$.data[1].categoryId") { value(category2.categoryId) }
                    jsonPath("$.data[1].name") { value(category2.name) }
                }
        }

    }

}
