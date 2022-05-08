package com.depromeet.threedollar.domain.rds.user.domain.faq;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.depromeet.threedollar.domain.rds.user.domain.TestFixture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@TestFixture
public class FaqCreator {

    @Builder
    public static Faq create(
        @NotNull String question,
        @NotNull String answer,
        FaqCategory category
    ) {
        return Faq.builder()
            .question(question)
            .answer(answer)
            .category(Optional.ofNullable(category).orElse(FaqCategory.ETC))
            .build();
    }

}
