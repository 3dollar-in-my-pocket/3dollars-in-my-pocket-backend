package com.depromeet.threedollar.domain.rds.domain.commonservice.faq

import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.domain.rds.domain.TestFixture

@TestFixture
object FaqFixture {

    @JvmOverloads
    @JvmStatic
    fun create(
        question: String,
        answer: String,
        category: FaqCategory,
        applicationType: ApplicationType = ApplicationType.USER_API,
    ): Faq {
        return Faq.builder()
            .applicationType(applicationType)
            .question(question)
            .answer(answer)
            .category(category)
            .build()
    }

}
