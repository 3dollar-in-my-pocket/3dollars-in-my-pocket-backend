package com.depromeet.threedollar.boss.api.controller.category

import com.depromeet.threedollar.boss.api.service.store.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryCreator
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class BossStoreCategoryControllerTest(
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val mockMvc: MockMvc
) {

    @AfterEach
    fun cleanUp() {
        bossStoreCategoryRepository.deleteAll()
    }

    @Test
    fun `등록된 사장님 가게의 카테고리 목록을 조회한다 priority가 낮은 거부터 조회된다`() {
        // given
        val category1 = BossStoreCategoryCreator.create(title = "한식", sequencePriority = 1)
        val category2 = BossStoreCategoryCreator.create(title = "중식", sequencePriority = 2)
        bossStoreCategoryRepository.saveAll(listOf(category1, category2))

        // when & then
        mockMvc.get("/v1/boss-store/categories")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data") { hasSize<BossStoreCategoryResponse>(2) }
                jsonPath("$.data[0].categoryId") { value(category1.id) }
                jsonPath("$.data[0].title") { value(category1.title) }
                jsonPath("$.data[1].categoryId") { value(category2.id) }
                jsonPath("$.data[1].title") { value(category2.title) }
            }
    }

}