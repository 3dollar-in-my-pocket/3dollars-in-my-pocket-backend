package com.depromeet.threedollar.api.vendor.controller.boss.category

import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import com.depromeet.threedollar.api.core.service.foodtruck.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.api.vendor.controller.SetupControllerTest
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.category.BossStoreCategoryCreator
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.redis.domain.boss.category.BossStoreCategoryCacheModel
import com.depromeet.threedollar.domain.redis.domain.boss.category.BossStoreCategoryCacheRepository
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every

internal class BossStoreCategoryControllerTest(
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
) : SetupControllerTest() {

    @SpykBean
    private lateinit var bossStoreCategoryCacheRepository: BossStoreCategoryCacheRepository

    @AfterEach
    fun cleanUp() {
        bossStoreCategoryRepository.deleteAll()
    }

    @Test
    fun `카테고리 목록이 캐시에 없는 경우, MongoDB에서 가져온 데이터를 반환한다`() {
        // given
        every { bossStoreCategoryCacheRepository.set(any()) } returns Unit
        every { bossStoreCategoryCacheRepository.getAll() } returns null

        val category1 = BossStoreCategoryCreator.create(title = "한식", sequencePriority = 1)
        val category2 = BossStoreCategoryCreator.create(title = "중식", sequencePriority = 2)
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
        every { bossStoreCategoryCacheRepository.set(any()) } returns Unit
        every { bossStoreCategoryCacheRepository.getAll() } returns listOf()

        val category1 = BossStoreCategoryCreator.create(title = "한식", sequencePriority = 1)
        val category2 = BossStoreCategoryCreator.create(title = "중식", sequencePriority = 2)
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
        val category1 = BossStoreCategoryCacheModel.of(categoryId = "categoryId1", name = "한식")
        val category2 = BossStoreCategoryCacheModel.of(categoryId = "categoryId1", name = "한식")

        every { bossStoreCategoryCacheRepository.set(any()) } returns Unit
        every { bossStoreCategoryCacheRepository.getAll() } returns listOf(category1, category2)

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
