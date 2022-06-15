package com.depromeet.threedollar.api.adminservice.service.userservice.faq

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import com.depromeet.threedollar.api.adminservice.IntegrationTest
import com.depromeet.threedollar.api.adminservice.service.commonservice.faq.FaqAdminService
import com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.request.AddFaqRequest
import com.depromeet.threedollar.api.adminservice.service.commonservice.faq.dto.request.UpdateFaqRequest
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.Faq
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCreator
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqRepository

internal class FaqAdminServiceTest(
    private val faqAdminService: FaqAdminService,
    private val faqRepository: FaqRepository,
) : IntegrationTest() {

    @DisplayName("신규 FAQ 등록")
    @Nested
    inner class AddFAQTest {

        @Test
        fun 새로운_FAQ_를_등록하면_FAQ_데이터가_추가된다() {
            // given
            val applicationType = ApplicationType.BOSS_API
            val question = "이름이 뭔가요?"
            val answer = "가슴속 삼천원입니다"
            val category = FaqCategory.ETC

            val request = AddFaqRequest(
                applicationType = applicationType,
                question = question,
                answer = answer,
                category = category
            )

            // when
            faqAdminService.addFaq(request)

            // then
            val faqs = faqRepository.findAll()
            assertAll({
                assertThat(faqs).hasSize(1)
                assertFaq(faqs[0], applicationType = applicationType, question = question, answer = answer, category = category)
            })
        }

        @Test
        fun `해당하는 서비스에서 지원하지 않는 FAQ 카테고리인경우 ForbiddenException이 발생한다`() {
            // given
            val applicationType = ApplicationType.BOSS_API
            val question = "이름이 뭔가요?"
            val answer = "가슴속 삼천원입니다"
            val category = FaqCategory.REVIEW_MENU

            val request = AddFaqRequest(
                applicationType = applicationType,
                question = question,
                answer = answer,
                category = category
            )

            // when & then
            assertThatThrownBy { faqAdminService.addFaq(request) }.isInstanceOf(ForbiddenException::class.java)
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

            val faq = FaqCreator.create(
                question = "기존의 질문",
                answer = "기존의 답변",
                category = FaqCategory.CATEGORY)
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
                assertFaq(faqs[0], applicationType = faq.applicationType, question = question, answer = answer, category = category)
            })
        }

        @Test
        fun `FAQ 수정시 해당하는 서비스에서 지원하지 않는 FAQ 카테고리인경우 ForbiddenException이 발생한다`() {
            // given
            val faq = FaqCreator.create(
                applicationType = ApplicationType.BOSS_API,
                question = "기존의 질문",
                answer = "기존의 답변",
                category = FaqCategory.ETC)
            faqRepository.save(faq)

            val request = UpdateFaqRequest(question = "질문", answer = "답변", category = FaqCategory.REVIEW_MENU)

            // when & then
            assertThatThrownBy {
                faqAdminService.updateFaq(
                    faqId = faq.id,
                    request = request
                )
            }.isInstanceOf(ForbiddenException::class.java)
        }

        @Test
        fun 등록된_FAQ를_수정할때_해당하는_FAQ가_없으면_NotFOUND_EXCEPTION() {
            // given
            val notFoundFaqId = -1L
            val request = UpdateFaqRequest(question = "질문", answer = "답변", category = FaqCategory.REVIEW_MENU)

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

    private fun assertFaq(faq: Faq, applicationType: ApplicationType, question: String, answer: String, category: FaqCategory) {
        assertThat(faq.applicationType).isEqualTo(applicationType)
        assertThat(faq.question).isEqualTo(question)
        assertThat(faq.answer).isEqualTo(answer)
        assertThat(faq.category).isEqualTo(category)
    }

}
