package com.depromeet.threedollar.api.user.controller.boss.category

import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import com.depromeet.threedollar.api.core.service.boss.category.dto.response.BossStoreCategoryResponse
import com.depromeet.threedollar.api.user.controller.SetupControllerTest
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryCreator
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository

internal class BossStoreCategoryControllerTest(
    private val bossStoreCategoryRepository: BossStoreCategoryRepository
) : SetupControllerTest() {

    @AfterEach
    fun cleanUp() {
        bossStoreCategoryRepository.deleteAll()
    }

    @DisplayName("GET /api/v1/boss/store/categories")
    @Test
    fun `등록된 사장님 가게의 카테고리 목록을 조회한다 priority가 낮은 거부터 조회된다`() {
        // given
        val categoryPriorityOne = BossStoreCategoryCreator.create(title = "한식", sequencePriority = 1)
        val categoryPriorityTwo = BossStoreCategoryCreator.create(title = "중식", sequencePriority = 2)
        val categoryPriorityThree = BossStoreCategoryCreator.create(title = "일식", sequencePriority = 3)
        bossStoreCategoryRepository.saveAll(listOf(categoryPriorityOne, categoryPriorityTwo, categoryPriorityThree))

        // when & then
        mockMvc.get("/v1/boss/store/categories")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.data", hasSize<BossStoreCategoryResponse>(3))
                jsonPath("$.data[0].categoryId") { value(categoryPriorityOne.id) }
                jsonPath("$.data[0].name") { value(categoryPriorityOne.name) }

                jsonPath("$.data[1].categoryId") { value(categoryPriorityTwo.id) }
                jsonPath("$.data[1].name") { value(categoryPriorityTwo.name) }

                jsonPath("$.data[2].categoryId") { value(categoryPriorityThree.id) }
                jsonPath("$.data[2].name") { value(categoryPriorityThree.name) }
            }
    }

}
