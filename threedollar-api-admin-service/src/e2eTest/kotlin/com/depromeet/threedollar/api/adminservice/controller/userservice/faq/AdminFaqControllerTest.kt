package com.depromeet.threedollar.api.adminservice.controller.userservice.faq

import com.depromeet.threedollar.api.adminservice.SetupAdminControllerTest
import com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.api.core.service.mapper.commonservice.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.api.core.service.service.commonservice.faq.dto.response.FaqResponse
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqFixture
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqRepository
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

internal class AdminFaqControllerTest(
    private val faqRepository: FaqRepository,
) : SetupAdminControllerTest() {

    @DisplayName("POST /admin/v1/faq")
    @Nested
    inner class AddFaqApiTest {

        @Test
        fun 새로운_FAQ를_추가한다() {
            // given
            val request = AddFaqRequest(
                applicationType = ApplicationType.BOSS_API,
                question = "카테고리 질문",
                answer = "카테고리 답변",
                category = FaqCategory.ETC
            )

            // when & then
            mockMvc.post("/v1/faq") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }

                    jsonPath("$.data.faqId") { isNotEmpty() }
                    jsonPath("$.data.question") { value(request.question) }
                    jsonPath("$.data.answer") { value(request.answer) }
                    jsonPath("$.data.category") { value(request.category.name) }
                }
        }

    }

    @DisplayName("PUT /admin/v1/faq")
    @Nested
    inner class UpdateFaqApiTest {

        @Test
        fun FAQ_를_수정한다() {
            // given
            val faq = faqRepository.save(FaqFixture.create("question", "answer", FaqCategory.BOARD))

            val request = UpdateFaqRequest("카테고리 질문", "카테고리 답변", FaqCategory.CATEGORY)

            // when & then
            mockMvc.put("/v1/faq/${faq.id}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }

                    jsonPath("$.data.faqId") { value(faq.id) }
                    jsonPath("$.data.question") { value(request.question) }
                    jsonPath("$.data.answer") { value(request.answer) }
                    jsonPath("$.data.category") { value(request.category.name) }
                }
        }

    }

    @DisplayName("DELETE /admin/v1/faq")
    @Nested
    inner class DeleteFaqApiTest {

        @Test
        fun FAQ_를_삭제한다() {
            // given
            val faq = faqRepository.save(FaqFixture.create("question", "answer", FaqCategory.BOARD))

            // when & then
            mockMvc.delete("/v1/faq/${faq.id}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }

                    jsonPath("$.data") { value(ApiResponse.OK.data) }
                }
        }

    }

    @DisplayName("GET /admin/v1/faqs")
    @Nested
    inner class RetrieveFaqsApiTest {

        @Test
        fun FAQ_리스트를_조회한다() {
            // given
            val faq1 = FaqFixture.create(applicationType = ApplicationType.USER_API, question = "question1", answer = "answer1", category = FaqCategory.BOARD)
            val faq2 = FaqFixture.create(applicationType = ApplicationType.BOSS_API, question = "question2", answer = "answer2", category = FaqCategory.CATEGORY)
            faqRepository.saveAll(listOf(faq1, faq2))

            // when & then
            mockMvc.get("/v1/faqs") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }

                    jsonPath("$.data", hasSize<FaqResponse>(2))

                    jsonPath("$.data[0].faqId") { value(faq1.id) }
                    jsonPath("$.data[0].question") { value(faq1.question) }
                    jsonPath("$.data[0].answer") { value(faq1.answer) }
                    jsonPath("$.data[0].applicationType") { value(faq1.applicationType.name) }
                    jsonPath("$.data[0].category.category") { value(faq1.category.name) }
                    jsonPath("$.data[0].category.description") { value(faq1.category.description) }
                    jsonPath("$.data[0].category.displayOrder") { value(faq1.category.displayOrder) }

                    jsonPath("$.data[1].faqId") { value(faq2.id) }
                    jsonPath("$.data[1].question") { value(faq2.question) }
                    jsonPath("$.data[1].answer") { value(faq2.answer) }
                    jsonPath("$.data[1].applicationType") { value(faq2.applicationType.name) }
                    jsonPath("$.data[1].category.category") { value(faq2.category.name) }
                    jsonPath("$.data[1].category.description") { value(faq2.category.description) }
                    jsonPath("$.data[1].category.displayOrder") { value(faq2.category.displayOrder) }
                }
        }

    }

    @DisplayName("GET /admin/v1/faq/categories")
    @Nested
    inner class RetrieveFaqCategoriesApiTest {

        @Test
        fun FAQ_카테고리_리스트를_조회한다() {
            // when & then
            mockMvc.get("/v1/faq/categories") {
                param("applicationType", ApplicationType.USER_API.name)
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }

                    jsonPath("$.data", hasSize<FaqCategoryResponse>(6))
                }
        }

    }

}
