package com.depromeet.threedollar.api.core.service.commonservice.faq

import com.depromeet.threedollar.api.core.IntegrationTest
import com.depromeet.threedollar.api.core.service.commonservice.faq.dto.request.RetrieveFaqsRequest
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.commonservice.faq.FaqCategory
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class FaqServiceTest(
    private val faqService: FaqService,
) : IntegrationTest() {

    @Test
    fun `카테고리 목록을 조회할 때, 해당 서비스에서 지원하지 않는 카테고리를 요청하면 Forbidden 에러가 발생한다`() {
        // given
        val applicationType = ApplicationType.BOSS_API

        val request = RetrieveFaqsRequest(category = FaqCategory.REVIEW_MENU)

        // when & then
        assertThatThrownBy {
            faqService.retrieveFaqsByCategory(applicationType = applicationType, request = request)
        }.isInstanceOf(ForbiddenException::class.java)
    }

    @Test
    fun `카테고리 목록을 조회할 때, 해당 서비스에서 지원하는 카테고리를 요청하면 Forbidden 에러가 발생하지 않는다`() {
        // given
        val applicationType = ApplicationType.USER_API

        val request = RetrieveFaqsRequest(category = FaqCategory.REVIEW_MENU)

        // when & then
        assertDoesNotThrow { faqService.retrieveFaqsByCategory(applicationType = applicationType, request = request) }
    }

}
