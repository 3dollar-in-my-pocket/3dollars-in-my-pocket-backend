package com.depromeet.threedollar.domain.rds.user.domain.faq;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.Builder;

@TestFixture
public class FaqCreator {

    @Builder
    public static Faq create(
        @NotNull String question,
        @NotNull String answer,
        @NotNull FaqCategory category
    ) {
        return Faq.builder()
            .question(question)
            .answer(answer)
            .category(category)
            .build();
    }

}
