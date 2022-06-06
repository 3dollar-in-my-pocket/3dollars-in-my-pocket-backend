package com.depromeet.threedollar.api.admin.controller.userservice.faq

import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import com.depromeet.threedollar.api.admin.controller.SetupAdminControllerTest
import com.depromeet.threedollar.api.admin.service.userservice.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.api.admin.service.userservice.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.service.userservice.faq.dto.request.RetrieveFaqsRequest
import com.depromeet.threedollar.api.core.service.userservice.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.api.core.service.userservice.faq.dto.response.FaqResponse
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCreator
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqRepository

internal class AdminFaqControllerTest(
    private val faqRepository: FaqRepository,
) : SetupAdminControllerTest() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        faqRepository.deleteAllInBatch()
    }

    @DisplayName("POST /admin/v1/user/faq")
    @Nested
    inner class AddFaqApiTest {

        @Test
        fun 새로운_FAQ를_추가한다() {
            // given
            val request = AddFaqRequest("카테고리 질문", "카테고리 답변", FaqCategory.CATEGORY)

            // when & then
            mockMvc.post("/v1/user/faq") {
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

    @DisplayName("PUT /admin/v1/user/faq")
    @Nested
    inner class UpdateFaqApiTest {

        @Test
        fun FAQ_를_수정한다() {
            // given
            val faq = faqRepository.save(FaqCreator.create("question", "answer", FaqCategory.BOARD))

            val request = UpdateFaqRequest("카테고리 질문", "카테고리 답변", FaqCategory.CATEGORY)

            // when & then
            mockMvc.put("/v1/user/faq/${faq.id}") {
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

    @DisplayName("DELETE /admin/v1/user/faq")
    @Nested
    inner class DeleteFaqApiTest {

        @Test
        fun FAQ_를_삭제한다() {
            // given
            val faq = faqRepository.save(FaqCreator.create("question", "answer", FaqCategory.BOARD))

            // when & then
            mockMvc.delete("/v1/user/faq/${faq.id}") {
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

    @DisplayName("GET /admin/v1/user/faqs")
    @Nested
    inner class RetrieveFaqsApiTest {

        @Test
        fun FAQ_리스트를_조회한다() {
            // given
            val faq1 = FaqCreator.create("question1", "answer1", FaqCategory.BOARD)
            val faq2 = FaqCreator.create("question2", "answer2", FaqCategory.CATEGORY)
            faqRepository.saveAll(listOf(faq1, faq2))

            // when & then
            mockMvc.get("/v1/user/faqs") {
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
                    jsonPath("$.data[0].category") { value(faq1.category.name) }

                    jsonPath("$.data[1].faqId") { value(faq2.id) }
                    jsonPath("$.data[1].question") { value(faq2.question) }
                    jsonPath("$.data[1].answer") { value(faq2.answer) }
                    jsonPath("$.data[1].category") { value(faq2.category.name) }
                }
        }

        @Test
        fun 특정_카테고리의_FAQ_리스트를_조회한다() {
            // given
            val faq1 = FaqCreator.create("question1", "answer1", FaqCategory.BOARD)
            val faq2 = FaqCreator.create("question2", "answer2", FaqCategory.CATEGORY)
            faqRepository.saveAll(listOf(faq1, faq2))

            val request = RetrieveFaqsRequest(FaqCategory.CATEGORY)

            // when & then
            mockMvc.get("/v1/user/faqs?category=${request.category}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }

                    jsonPath("$.data", hasSize<FaqResponse>(1))
                    jsonPath("$.data[0].faqId") { value(faq2.id) }
                    jsonPath("$.data[0].question") { value(faq2.question) }
                    jsonPath("$.data[0].answer") { value(faq2.answer) }
                    jsonPath("$.data[0].category") { value(faq2.category.name) }
                }
        }

    }

    @DisplayName("GET /admin/v1/user/faq/categories")
    @Nested
    inner class RetrieveFaqCategoriesApiTest {

        @Test
        fun FAQ_카테고리_리스트를_조회한다() {
            // when & then
            mockMvc.get("/v1/user/faq/categories") {
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
