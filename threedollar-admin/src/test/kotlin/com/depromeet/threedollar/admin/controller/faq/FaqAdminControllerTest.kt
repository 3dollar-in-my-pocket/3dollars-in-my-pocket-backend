package com.depromeet.threedollar.admin.controller.faq

import com.depromeet.threedollar.admin.controller.ControllerTestUtils
import com.depromeet.threedollar.admin.service.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.admin.service.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.application.mapper.faq.dto.response.FaqCategoryResponse
import com.depromeet.threedollar.application.service.faq.dto.request.RetrieveFaqsRequest
import com.depromeet.threedollar.application.service.faq.dto.response.FaqResponse
import com.depromeet.threedollar.common.exception.ErrorCode.*
import com.depromeet.threedollar.domain.domain.faq.FaqCategory
import com.depromeet.threedollar.domain.domain.faq.FaqCreator
import com.depromeet.threedollar.domain.domain.faq.FaqRepository
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.*
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
internal class FaqAdminControllerTest(
    private val faqRepository: FaqRepository
) : ControllerTestUtils() {

    @AfterEach
    fun cleanUp() {
        super.cleanup()
        faqRepository.deleteAll()
    }

    @DisplayName("POST /admin/v1/faq")
    @Nested
    inner class AddFaq {

        @Test
        fun 새로운_FAQ를_추가한다() {
            // given
            val request = AddFaqRequest("카테고리 질문", "카테고리 답변", FaqCategory.CATEGORY)

            // when & then
            mockMvc.post("/admin/v1/faq") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }
                    content {
                        jsonPath("$.data.faqId") { isNotEmpty() }
                        jsonPath("$.data.question") { value(request.question) }
                        jsonPath("$.data.answer") { value(request.answer) }
                        jsonPath("$.data.category") { value(request.category?.name) }
                    }
                }
        }

        @Test
        fun 잘못된_토큰인경우_401에러() {
            // given
            val request = AddFaqRequest("카테고리 질문", "카테고리 답변", FaqCategory.CATEGORY)

            // when & then
            mockMvc.post("/admin/v1/faq") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isUnauthorized() }
                    content {
                        jsonPath("$.resultCode") { value(UNAUTHORIZED_EXCEPTION.code) }
                        jsonPath("$.message") { value(UNAUTHORIZED_EXCEPTION.message) }
                    }
                }
        }

    }

    @DisplayName("PUT /admin/v1/faq")
    @Nested
    inner class UpdateFaq {

        @Test
        fun FAQ_를_수정한다() {
            // given
            val faq = faqRepository.save(FaqCreator.create("question", "answer", FaqCategory.BOARD))

            val request = UpdateFaqRequest("카테고리 질문", "카테고리 답변", FaqCategory.CATEGORY)

            // when & then
            mockMvc.put("/admin/v1/faq/${faq.id}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }
                    content {
                        jsonPath("$.data.faqId") { value(faq.id) }
                        jsonPath("$.data.question") { value(request.question) }
                        jsonPath("$.data.answer") { value(request.answer) }
                        jsonPath("$.data.category") { value(request.category?.name) }
                    }
                }
        }

        @Test
        fun FAQ_수정시_존재하지_않는_FAQ인경우_404_NotFound() {
            // given
            val faqId = 99999L
            val request = UpdateFaqRequest("카테고리 질문", "카테고리 답변", FaqCategory.CATEGORY)

            // when & then
            mockMvc.put("/admin/v1/faq/${faqId}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isNotFound() }
                    content {
                        jsonPath("$.resultCode") { value(NOT_FOUND_FAQ_EXCEPTION.code) }
                        jsonPath("$.message") { value(NOT_FOUND_FAQ_EXCEPTION.message) }
                    }
                }
        }

    }

    @DisplayName("DELETE /admin/v1/faq")
    @Nested
    inner class DeleteFaq {

        @Test
        fun FAQ_를_삭제한다() {
            // given
            val faq = faqRepository.save(FaqCreator.create("question", "answer", FaqCategory.BOARD))

            // when & then
            mockMvc.delete("/admin/v1/faq/${faq.id}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }
                    content {
                        jsonPath("$.data") { value(ApiResponse.SUCCESS.data) }
                    }
                }
        }

        @Test
        fun FAQ_삭제시_존재하지_않는_FAQ인경우_404_NotFound() {
            // given
            val faqId = 99999L

            // when & then
            mockMvc.delete("/admin/v1/faq/${faqId}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isNotFound() }
                    content {
                        jsonPath("$.resultCode") { value(NOT_FOUND_FAQ_EXCEPTION.code) }
                        jsonPath("$.message") { value(NOT_FOUND_FAQ_EXCEPTION.message) }
                    }
                }
        }

    }

    @DisplayName("GET /admin/v1/faqs")
    @Nested
    inner class RetrieveFaqs {

        @Test
        fun FAQ_리스트를_조회한다() {
            // given
            val faq1 = FaqCreator.create("question1", "answer1", FaqCategory.BOARD)
            val faq2 = FaqCreator.create("question2", "answer2", FaqCategory.CATEGORY)
            faqRepository.saveAll(listOf(faq1, faq2))

            // when & then
            mockMvc.get("/admin/v1/faqs") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }
                    content {
                        jsonPath("$.data") { hasSize<FaqResponse>(2) }

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
        }

        @Test
        fun 특정_카테고리의_FAQ_리스트를_조회한다() {
            // given
            val faq1 = FaqCreator.create("question1", "answer1", FaqCategory.BOARD)
            val faq2 = FaqCreator.create("question2", "answer2", FaqCategory.CATEGORY)
            faqRepository.saveAll(listOf(faq1, faq2))

            val request = RetrieveFaqsRequest(FaqCategory.CATEGORY)

            // when & then
            mockMvc.get("/admin/v1/faqs?category=${request.category}") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }
                    content {
                        jsonPath("$.data") { hasSize<FaqResponse>(1) }
                        jsonPath("$.data[0].faqId") { value(faq2.id) }
                        jsonPath("$.data[0].question") { value(faq2.question) }
                        jsonPath("$.data[0].answer") { value(faq2.answer) }
                        jsonPath("$.data[0].category") { value(faq2.category.name) }
                    }
                }
        }

    }

    @DisplayName("GET /admin/v1/faq-categories")
    @Nested
    inner class RetrieveFaqCategories {

        @Test
        fun FAQ_카테고리_리스트를_조회한다() {
            // when & then
            mockMvc.get("/admin/v1/faq/categories") {
                header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            }
                .andDo {
                    print()
                }
                .andExpect {
                    status { isOk() }
                    content {
                        jsonPath("$.data") { hasSize<FaqCategoryResponse>(6) }
                    }
                }
        }

    }

}
