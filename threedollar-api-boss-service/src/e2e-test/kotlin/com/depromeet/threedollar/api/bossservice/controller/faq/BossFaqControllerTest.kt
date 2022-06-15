package com.depromeet.threedollar.api.bossservice.controller.faq

import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import com.depromeet.threedollar.api.bossservice.SetupControllerTest
import com.depromeet.threedollar.api.core.service.commonservice.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.api.core.service.commonservice.faq.dto.response.FaqResponse
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCreator
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqRepository

internal class BossFaqControllerTest(
    private val faqRepository: FaqRepository,
) : SetupControllerTest() {

    @AfterEach
    fun cleanUp() {
        faqRepository.deleteAll()
    }

    @Nested
    inner class RetrieveFaqsApiTest {

        @Test
        fun `FAQ 목록을 조회합니다`() {
            // given
            val faq1 = FaqCreator.create(
                applicationType = ApplicationType.BOSS_API,
                question = "질문1",
                answer = "답변1",
                category = FaqCategory.ETC
            )
            val faq2 = FaqCreator.create(
                applicationType = ApplicationType.BOSS_API,
                question = "질문2",
                answer = "답변2",
                category = FaqCategory.BOARD
            )
            faqRepository.saveAll(listOf(faq1, faq2))

            // when & then
            mockMvc.get("/v1/faqs")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data", hasSize<FaqResponse>(2))

                    jsonPath("$.data[0].faqId") { value(faq2.id) }
                    jsonPath("$.data[0].question") { value(faq2.question) }
                    jsonPath("$.data[0].answer") { value(faq2.answer) }
                    jsonPath("$.data[0].category") { value(faq2.category.name) }

                    jsonPath("$.data[1].faqId") { value(faq1.id) }
                    jsonPath("$.data[1].question") { value(faq1.question) }
                    jsonPath("$.data[1].answer") { value(faq1.answer) }
                    jsonPath("$.data[1].category") { value(faq1.category.name) }
                }
        }

        @Test
        fun `카테고리에 해당하는 FAQ 목록을 조회합니다`() {
            // given
            val faq1 = FaqCreator.create(
                applicationType = ApplicationType.BOSS_API,
                question = "질문1",
                answer = "답변1",
                category = FaqCategory.ETC
            )
            val faq2 = FaqCreator.create(
                applicationType = ApplicationType.BOSS_API,
                question = "질문2",
                answer = "답변2",
                category = FaqCategory.BOARD
            )
            faqRepository.saveAll(listOf(faq1, faq2))

            // when & then
            mockMvc.get("/v1/faqs") {
                param("category", FaqCategory.ETC.name)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data", hasSize<FaqResponse>(1))

                    jsonPath("$.data[0].faqId") { value(faq1.id) }
                    jsonPath("$.data[0].question") { value(faq1.question) }
                    jsonPath("$.data[0].answer") { value(faq1.answer) }
                    jsonPath("$.data[0].category") { value(faq1.category.name) }
                }
        }

        @Test
        fun `사장님 서비스에 지원하지 않는 FAQ 카테고리를 파라미터로 넘기면 403 에러가 발생한다`() {
            // when & then
            mockMvc.get("/v1/faqs") {
                param("category", FaqCategory.REVIEW_MENU.name)
            }
                .andDo { print() }
                .andExpect {
                    status { isForbidden() }
                    jsonPath("$.resultCode") { value(ErrorCode.FORBIDDEN_NOT_SUPPORTED_FAQ_CATEGORY.code) }
                    jsonPath("$.message") { value(ErrorCode.FORBIDDEN_NOT_SUPPORTED_FAQ_CATEGORY.message) }
                }
        }

    }

    @Nested
    inner class RetrieveFaqCategoriesApiTest {

        @Test
        fun `FAQ 카테고리 목록을 조회한다`() {
            // when & then
            mockMvc.get("/v1/faq/categories")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.data", hasSize<FaqCategoryResponse>(1))
                    jsonPath("$.data[0].category") { value(FaqCategory.ETC.name) }
                    jsonPath("$.data[0].description") { value(FaqCategory.ETC.description) }
                    jsonPath("$.data[0].displayOrder") { value(FaqCategory.ETC.displayOrder) }
                }
        }

    }

}
