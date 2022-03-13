package com.depromeet.threedollar.api.boss.controller.category

import com.depromeet.threedollar.api.core.service.boss.store.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
internal class BossStoreCategoryControllerTest(
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val mockMvc: MockMvc
) {

    @AfterEach
    fun cleanUp() {
        bossStoreCategoryRepository.deleteAll()
    }

    @DisplayName("GET /boss/v1/boss/store/categories")
    @Test
    fun `등록된 사장님 가게의 카테고리 목록을 조회한다 priority가 낮은 거부터 조회된다`() {
        // given
        val category1 = BossStoreCategoryCreator.create(title = "한식", sequencePriority = 1)
        val category2 = BossStoreCategoryCreator.create(title = "중식", sequencePriority = 2)
        bossStoreCategoryRepository.saveAll(listOf(category1, category2))

        // when & then
        mockMvc.get("/v1/boss/store/categories")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { hasSize<BossStoreCategoryResponse>(2) }
                jsonPath("$.data[0].categoryId") { value(category1.id) }
                jsonPath("$.data[0].name") { value(category1.name) }
                jsonPath("$.data[1].categoryId") { value(category2.id) }
                jsonPath("$.data[1].name") { value(category2.name) }
            }
    }

}
