package com.depromeet.threedollar.admin.service.faq

import com.depromeet.threedollar.admin.service.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.admin.service.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.domain.faq.Faq
import com.depromeet.threedollar.domain.domain.faq.FaqCategory
import com.depromeet.threedollar.domain.domain.faq.FaqCreator
import com.depromeet.threedollar.domain.domain.faq.FaqRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class FaqAdminServiceTest(
    private val faqAdminService: FaqAdminService,
    private val faqRepository: FaqRepository
) {

    @AfterEach
    fun cleanUp() {
        faqRepository.deleteAll()
    }

    @Test
    fun 새로운_FAQ_를_등록하면_FAQ_데이터가_추가된다() {
        // give
        val question = "이름이 뭔가요?"
        val answer = "가슴속 삼천원"
        val category = FaqCategory.CATEGORY

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

    @Test
    fun 등록된_FAQ를_수정하면_FAQ_데이터가_수정된다() {
        // give
        val question = "질문"
        val answer = "답변"
        val category = FaqCategory.CATEGORY

        val faq = FaqCreator.create("question", "answer", FaqCategory.CATEGORY)
        faqRepository.save(faq)

        val request = UpdateFaqRequest(question, answer, category)

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
        val request = UpdateFaqRequest("question", "answer", FaqCategory.CATEGORY)

        // when & then
        assertThatThrownBy { faqAdminService.updateFaq(999L, request) }.isInstanceOf(NotFoundException::class.java)
    }

    @Test
    fun 특정_FAQ_를_삭제하면_해당_데이터가_삭제된다() {
        // given
        val faq = FaqCreator.create("question", "answer", FaqCategory.CATEGORY)
        faqRepository.save(faq)

        // when
        faqAdminService.deleteFaq(faq.id)

        // then
        val faqs = faqRepository.findAll()
        assertThat(faqs).isEmpty()
    }

    @Test
    fun 특정_FAQ_를_삭제시_해당_FAQ가_없으면_NOTFOUND_EXCEPTION() {
        // when & then
        assertThatThrownBy { faqAdminService.deleteFaq(999L) }.isInstanceOf(NotFoundException::class.java)
    }

    private fun assertFaq(faq: Faq, question: String, answer: String, category: FaqCategory) {
        assertThat(faq.question).isEqualTo(question)
        assertThat(faq.answer).isEqualTo(answer)
        assertThat(faq.category).isEqualTo(category)
    }

}
