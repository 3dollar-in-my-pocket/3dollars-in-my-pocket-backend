package com.depromeet.threedollar.domain.user.domain.faq;

import com.depromeet.threedollar.common.docs.ObjectMother;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ObjectMother
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FaqCreator {

    public static Faq create(String question, String answer, FaqCategory category) {
        return Faq.newInstance(category, question, answer);
    }

}
