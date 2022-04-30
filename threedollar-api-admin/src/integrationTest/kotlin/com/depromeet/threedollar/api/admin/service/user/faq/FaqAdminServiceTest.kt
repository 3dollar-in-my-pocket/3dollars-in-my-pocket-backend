package com.depromeet.threedollar.api.admin.service.user.faq

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import com.depromeet.threedollar.api.admin.service.user.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.api.admin.service.user.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.rds.user.domain.faq.Faq
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqCategory
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqCreator
import com.depromeet.threedollar.domain.rds.user.domain.faq.FaqRepository

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class FaqAdminServiceTest(
    private val faqAdminService: FaqAdminService,
    private val faqRepository: FaqRepository
) {

    @AfterEach
    fun cleanUp() {
        faqRepository.deleteAllInBatch()
    }

    @DisplayName("신규 FAQ 등록")
    @Nested
    inner class AddFAQTest {

        @Test
        fun 새로운_FAQ_를_등록하면_FAQ_데이터가_추가된다() {
            // given
            val question = "이름이 뭔가요?"
            val answer = "가슴속 삼천원입니다"
            val category = FaqCategory.ETC

            val request = AddFaqRequest(question, answer, category)

            // when
            faqAdminService.addFaq(request)

            // then
            val faqs = faqRepository.findAll()
            assertAll({
                assertThat(faqs).hasSize(1)
                assertFaq(faqs[0], question, answer, category)
            })
        }

    }

    @DisplayName("FAQ 수정")
    @Nested
    inner class UpdateFaqTest {

        @Test
        fun 등록된_FAQ를_수정하면_FAQ_데이터가_수정된다() {
            // given
            val question = "이름이 뭔가요?"
            val answer = "가슴속 삼천원입니다"
            val category = FaqCategory.ETC

            val faq = FaqCreator.create("기존의 질문", "기존의 답변", FaqCategory.CATEGORY)
            faqRepository.save(faq)

            val request = UpdateFaqRequest(
                question = question,
                answer = answer,
                category = category
            )

            // when
            faqAdminService.updateFaq(faq.id, request)

            // then
            val faqs = faqRepository.findAll()
            assertAll({
                assertThat(faqs).hasSize(1)
                assertFaq(faqs[0], question, answer, category)
            })
        }

        @Test
        fun 등록된_FAQ를_수정할때_해당하는_FAQ가_없으면_NotFOUND_EXCEPTION() {
            // given
            val notFoundFaqId = -1L
            val request = UpdateFaqRequest("질문", "답변", FaqCategory.CATEGORY)

            // when & then
            assertThatThrownBy {
                faqAdminService.updateFaq(
                    faqId = notFoundFaqId,
                    request = request
                )
            }.isInstanceOf(NotFoundException::class.java)
        }

    }

    @DisplayName("FAQ 삭제")
    @Nested
    inner class DeleteFaqTest {

        @Test
        fun 특정_FAQ_를_삭제하면_해당_데이터가_삭제된다() {
            // given
            val faq = faqRepository.save(FaqCreator.create(question = "질문", answer = "답변", category = FaqCategory.CATEGORY))

            // when
            faqAdminService.deleteFaq(faq.id)

            // then
            val faqs = faqRepository.findAll()
            assertThat(faqs).isEmpty()
        }

        @Test
        fun 특정_FAQ_를_삭제시_해당_FAQ가_없으면_NOTFOUND_EXCEPTION() {
            // given
            val notFoundFaqId = -1L

            // when & then
            assertThatThrownBy { faqAdminService.deleteFaq(notFoundFaqId) }.isInstanceOf(NotFoundException::class.java)
        }

    }

    private fun assertFaq(faq: Faq, question: String, answer: String, category: FaqCategory) {
        assertThat(faq.question).isEqualTo(question)
        assertThat(faq.answer).isEqualTo(answer)
        assertThat(faq.category).isEqualTo(category)
    }

}
