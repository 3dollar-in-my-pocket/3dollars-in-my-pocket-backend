package com.depromeet.threedollar.domain.rds.user.domain.faq

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture

@TestFixture
object FaqCreator {

    @JvmStatic
    fun create(
        question: String,
        answer: String,
        category: FaqCategory,
    ): Faq {
        return Faq.builder()
            .question(question)
            .answer(answer)
            .category(category)
            .build()
    }

}
